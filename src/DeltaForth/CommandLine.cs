// <copyright file="CommandLine.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

using System;
using System.IO;

namespace DeltaForth
{
    /// <summary>
    /// Command line interface to the DeltaForth compiler.
    /// </summary>
    public class CommandLine
    {
        #region Compiler command-line option values

        private static bool displayLogo;
        private static bool quietMode;
        private static bool showTimings;
        private static bool generateExecutable;
        private static bool checkStack;
        private static bool displayMap;
        private static int forthStackSize = 524288;
        private static int returnStackSize = 1024;
        private static string sourceFile;       // Input filename
        private static string outputFile;       // Output filename
        private static string outputDirectory;  // Output directory
        private static string signatureFile;    // Signature file (snk)

        #endregion

        #region Local variables

        private static DateTime compilationTimeStart;
        private static DateTime compilationTimeEnd;
        private static DateTime partialTimeStart;
        private static DateTime partialTimeEnd;

        #endregion

        #region Display Methods

        /// <summary>
        /// Display text on the console with the specified color.
        /// </summary>
        /// <param name="text">Text to display to console.</param>
        /// <param name="color">Color of the text.</param>
        private static void DisplayLineToConsole(string text, ConsoleColor color)
        {
            if (!quietMode)
            {
                var oldConsoleColor = Console.ForegroundColor;
                Console.ForegroundColor = color;
                Console.WriteLine(text);
                Console.ForegroundColor = oldConsoleColor;
            }
        }

        /// <summary>
        /// Display text on the console with the default color.
        /// </summary>
        /// <param name="text">Text to display to console.</param>
        private static void DisplayLineToConsole(string text)
        {
            DisplayLineToConsole(text, ConsoleColor.Gray);
        }

        /// <summary>
        /// Display text on the console with the specified color.
        /// </summary>
        /// <param name="text">Text to display to console.</param>
        /// <param name="color">Color of the text.</param>
        private static void DisplayToConsole(string text, ConsoleColor color)
        {
            if (!quietMode)
            {
                var oldConsoleColor = Console.ForegroundColor;
                Console.ForegroundColor = color;
                Console.Write(text);
                Console.ForegroundColor = oldConsoleColor;
            }
        }

        /// <summary>
        /// Display text on the console with the default color.
        /// </summary>
        /// <param name="text">Text to display to console.</param>
        private static void DisplayToConsole(string text)
        {
            DisplayToConsole(text, ConsoleColor.Gray);
        }

        /// <summary>
        /// Display logo on the console.
        /// </summary>
        private static void DisplayLogoToConsole()
        {
            DisplayLineToConsole("DeltaForth Compiler, Version 1.4", ConsoleColor.Yellow);

            // DisplayLineToConsole("World's first Forth compiler for the .NET platform", ConsoleColor.White);
            DisplayLineToConsole("Copyright © 1997-2011 Valer BOCAN", ConsoleColor.White);
            DisplayLineToConsole("Copyright © 2018 Neil McNeight");
            DisplayLineToConsole("Web: http://www.bocan.ro/deltaforthnet");
        }

        private static void DisplayUsageToConsole()
        {
            DisplayLogoToConsole();
            DisplayLineToConsole("Usage: DeltaForth.exe <source file> [options]", ConsoleColor.Yellow);
            DisplayLineToConsole("\n\rOptions:");
            DisplayLineToConsole("/NOLOGO\t\t\tDon't type the logo");
            DisplayLineToConsole("/QUIET\t\t\tDon't report compiling progress");
            DisplayLineToConsole("/CLOCK\t\t\tMeasure and report compilation times");
            DisplayLineToConsole("/EXE\t\t\tCompile to EXE (default)");
            DisplayLineToConsole("/DLL\t\t\tCompile to DLL");
            DisplayLineToConsole("/NOCHECK\t\tDisable stack bounds checking");
            DisplayLineToConsole("/FS:<size>\t\tSpecify Forth stack size (default is 524288 cells)");
            DisplayLineToConsole("/RS:<size>\t\tSpecify return stack size (default is 1024 cells)");
            DisplayLineToConsole("/MAP\t\t\tGenerate detailed map information");
            DisplayLineToConsole("/OUTPUT=<targetfile>\tCompile to file with specified name\n\r\t\t\t(user must provide extension, if any)");
            DisplayLineToConsole("/KEY=<keyfile>\t\tCompile with strong signature\n\r\t\t\t(<keyfile> contains private key)");
            DisplayLineToConsole("\n\rDefault source file extension is .4th", ConsoleColor.Green);
        }

        #endregion

        private static void DisplayMapInformation(Compiler compiler)
        {
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("Summary of compilation objects", ConsoleColor.White);

            // Display global constants
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("Global constants:", ConsoleColor.Blue);
            foreach (var fc in compiler.MetaData.GlobalConstants)
            {
                if (fc.Value.GetType() != typeof(string))
                {
                    DisplayLineToConsole(string.Format("{0} = {1}", fc.Name, fc.Value));
                }
                else
                {
                    DisplayLineToConsole(string.Format("{0} = \"{1}\"", fc.Name, fc.Value));
                }
            }

            // Display global variables
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("Global variables:", ConsoleColor.Blue);
            foreach (var fv in compiler.MetaData.GlobalVariables)
            {
                DisplayLineToConsole(string.Format("{0} = (Addr:{1}, Size:{2})", fv.Name, fv.Address, fv.Size));
            }

            // Display local variables
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("Local variables:", ConsoleColor.Blue);
            foreach (var flv in compiler.MetaData.LocalVariables)
            {
                DisplayLineToConsole(string.Format("{0} = (Addr:{1}, Word:{2})", flv.Name, flv.Address, flv.WordName));
            }

            // Display words
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("Words:", ConsoleColor.Blue);
            foreach (var ew in compiler.MetaData.Words)
            {
                DisplayLineToConsole(string.Format("-> {0}", ew.Name));
            }

            // Display external words
            DisplayLineToConsole(string.Empty);
            DisplayLineToConsole("External words:", ConsoleColor.Blue);
            foreach (var ew in compiler.MetaData.ExternalWords)
            {
                DisplayLineToConsole(string.Format("{0} = (Library:{1}, Class:{2}, Method:{3})", ew.Name, ew.Library, ew.Class, ew.Method));
            }
        }

        private static void Main(string[] args)
        {
            // Initialize default parameter values
            displayLogo = generateExecutable = checkStack = true;
            quietMode = showTimings = displayMap = false;
            outputFile = outputDirectory = signatureFile = string.Empty;

            // Display usage screen if no parameters are given
            if (args.Length < 1)
            {
                DisplayUsageToConsole();
                return;
            }

            sourceFile = args[0];

            // Cycle through command line parameters
            for (var i = 1; i < args.Length; i++)
            {
                switch (args[i].ToUpper())
                {
                    case "/NOLOGO":
                        displayLogo = false;
                        break;
                    case "/QUIET":
                        quietMode = true;
                        break;
                    case "/CLOCK":
                        showTimings = true;
                        break;
                    case "/EXE":
                        generateExecutable = true;
                        break;
                    case "/DLL":
                        generateExecutable = false;
                        break;
                    case "/NOCHECK":
                        checkStack = false;
                        break;
                    case "/MAP":
                        displayMap = true;
                        break;
                    default:
                        if (args[i].ToUpper().StartsWith("/OUTPUT="))
                        {
                            outputFile = args[i].Substring(8);
                        }
                        else if (args[i].ToUpper().StartsWith("/FS:"))
                        {
                            try
                            {
                                forthStackSize = Convert.ToInt32(args[i].Substring(4));
                            }
                            catch (FormatException)
                            {
                                DisplayUsageToConsole();
                                return;
                            }
                        }
                        else if (args[i].ToUpper().StartsWith("/RS:"))
                        {
                            try
                            {
                                returnStackSize = Convert.ToInt32(args[i].Substring(4));
                            }
                            catch (FormatException)
                            {
                                DisplayUsageToConsole();
                                return;
                            }
                        }
                        else if (args[i].ToUpper().StartsWith("/KEY="))
                        {
                            signatureFile = args[i].Substring(5);
                        }
                        else
                        {
                            DisplayUsageToConsole();
                            return;
                        }

                        break;
                }
            }

            // Process parameters
            if (displayLogo)
            {
                DisplayLogoToConsole();
            }

            #region Input File Processing

            // If the extension for the input file is missing, add it
            if (!Path.HasExtension(sourceFile))
            {
                sourceFile = Path.ChangeExtension(sourceFile, ".4th");
            }
            #endregion

            #region Input File Existence Check

            // Check whether the input file really exists
            if (!File.Exists(sourceFile))
            {
                DisplayLineToConsole(string.Format("\n\rERROR: The file '{0}' could not be found.", sourceFile), ConsoleColor.Red);
                return;
            }
            #endregion

            #region Output File Processing

            // If there is no output file specified, generate a name for it based on the input file
            if (outputFile == string.Empty)
            {
                outputFile = Path.GetFileName(sourceFile);
                outputFile = Path.ChangeExtension(outputFile, generateExecutable ? ".exe" : ".dll");
                outputDirectory = Path.GetDirectoryName(sourceFile);
                if (outputDirectory == string.Empty)
                {
                    outputDirectory = ".";
                }
            }
            else
            {
                outputDirectory = Path.GetDirectoryName(outputFile);
                if (outputDirectory == string.Empty)
                {
                    outputDirectory = null;
                }

                outputFile = Path.GetFileName(outputFile);
            }
            #endregion

            var compiler = new Compiler();
            compiler.OnCodeGenerationStart += new Compiler.CompilerEventHandler(Compiler_OnCodeGenerationStart);
            compiler.OnCodeGenerationEnd += new Compiler.CompilerEventHandler(Compiler_OnCodeGenerationEnd);
            compiler.OnCompilationStart += new Compiler.CompilerEventHandler(Compiler_OnCompilationStart);
            compiler.OnCompilationEnd += new Compiler.CompilerEventHandler(Compiler_OnCompilationEnd);
            compiler.OnParsingEnd += new Compiler.CompilerEventHandler(Compiler_OnParsingEnd);
            compiler.OnParsingStart += new Compiler.CompilerEventHandler(Compiler_OnParsingStart);
            compiler.OnSyntacticAnalysisEnd += new Compiler.CompilerEventHandler(Compiler_OnSyntacticAnalysisEnd);
            compiler.OnSyntacticAnalysisStart += new Compiler.CompilerEventHandler(Compiler_OnSyntacticAnalysisStart);

            try
            {
                compiler.CompileFile(sourceFile, outputFile, outputDirectory, signatureFile, generateExecutable, checkStack, forthStackSize, returnStackSize);
            }
            catch (Exception e)
            {
                DisplayLineToConsole("\t\tFAILED", ConsoleColor.Red);
                DisplayLineToConsole(string.Format("\n\rCompilation error: {0}", e.Message), ConsoleColor.White);
                return;
            }

            if (displayMap)
            {
                DisplayMapInformation(compiler);
            }
        }

        #region Event Handlers

        private static void Compiler_OnSyntacticAnalysisStart(object sender, object e)
        {
            partialTimeStart = DateTime.Now;
            DisplayToConsole("Starting syntactic analysis...");
        }

        private static void Compiler_OnSyntacticAnalysisEnd(object sender, object e)
        {
            partialTimeEnd = DateTime.Now;

            if (showTimings)
            {
                DisplayToConsole("\t\tOK", ConsoleColor.Green);
                DisplayLineToConsole("\t(" + Math.Round((partialTimeEnd - partialTimeStart).TotalMilliseconds) + " ms)");
            }
            else
            {
                DisplayLineToConsole("\t\tOK", ConsoleColor.Green);
            }
        }

        private static void Compiler_OnParsingStart(object sender, object e)
        {
            partialTimeStart = DateTime.Now;
            DisplayToConsole("Parsing source file...");
        }

        private static void Compiler_OnParsingEnd(object sender, object e)
        {
            partialTimeEnd = DateTime.Now;
            if (showTimings)
            {
                DisplayToConsole("\t\t\tOK", ConsoleColor.Green);
                DisplayLineToConsole("\t(" + Math.Round((partialTimeEnd - partialTimeStart).TotalMilliseconds) + " ms)");
            }
            else
            {
                DisplayLineToConsole("\t\t\tOK", ConsoleColor.Green);
            }
        }

        private static void Compiler_OnCompilationStart(object sender, object e)
        {
            DisplayLineToConsole("Starting compilation...", ConsoleColor.White);
            DisplayLineToConsole(string.Empty);
            compilationTimeStart = DateTime.Now;
        }

        private static void Compiler_OnCompilationEnd(object sender, object e)
        {
            compilationTimeEnd = DateTime.Now;
            if (showTimings)
            {
                DisplayLineToConsole(string.Empty);
                DisplayToConsole("Compilation ended.", ConsoleColor.White);
                DisplayLineToConsole("\t\t\t\t(" + Math.Round((compilationTimeEnd - compilationTimeStart).TotalMilliseconds) + " ms)");
            }
            else
            {
                DisplayLineToConsole(string.Empty);
                DisplayLineToConsole("Compilation ended successfully.", ConsoleColor.White);
            }
        }

        private static void Compiler_OnCodeGenerationEnd(object sender, object e)
        {
            partialTimeEnd = DateTime.Now;
            if (showTimings)
            {
                DisplayToConsole("\t\tOK", ConsoleColor.Green);
                DisplayLineToConsole("\t(" + Math.Round((partialTimeEnd - partialTimeStart).TotalMilliseconds) + " ms)");
            }
            else
            {
                DisplayLineToConsole("\t\tOK", ConsoleColor.Green);
            }
        }

        private static void Compiler_OnCodeGenerationStart(object sender, object e)
        {
            partialTimeStart = DateTime.Now;
            DisplayToConsole("Generating executable code...");
        }
        #endregion
    }
}
