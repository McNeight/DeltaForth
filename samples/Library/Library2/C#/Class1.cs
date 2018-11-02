// <copyright file="Class1.cs" company="DeltaForth Code Samples">
// Licensed under the Creative Commons CC0 license.
// See the creative_commons_zero.markdown file in the samples directory for full license information.
// </copyright>

using System;
using System.IO;
using System.Reflection;

namespace Library2
{
    internal class Class1
    {
        private static Assembly calAssembly;
        private static Type calType;

        // Forth stack & index
        private static int[] ForthStack;
        private static int ForthStackIndex;

        [STAThread]
        private static void Main(string[] args)
        {
            if (!InitDLL("ForthLibrary.dll", "ForthLib"))
            {
                return;
            }

            Push(30);
            Push(18);
            CallForthWord("ADDITION");
            Console.WriteLine("Addition...");
            CallForthWord("DISPLAYTOPOFSTACK");
            Push(30);
            Push(18);
            CallForthWord("SUBTRACTION");
            Console.WriteLine("Subtraction...");
            CallForthWord("DISPLAYTOPOFSTACK");
        }

        // GetField - gets the value of a field defined in the Forth library
        private static object GetField(string FieldName)
        {
            var fi = calType.GetField(FieldName);
            if (fi == null)
            {
                Console.WriteLine("Runtime Error: Could not find field {0}.", FieldName);
                return null;
            }

            return fi.GetValue(null);
        }

        // InitDLL - Loads the Forth library and sets up the stack pointers
        private static bool InitDLL(string FileName, string ClassName)
        {
            try
            {
                calAssembly = Assembly.LoadFrom(FileName);
            }
            catch (FileLoadException fle)
            {
                Console.WriteLine("Error: {0}", fle.Message);
                return false;
            }

            try
            {
                calType = calAssembly.GetType(ClassName, true, true);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Runtime Error: Could not load class {0} from library. Reason: {1}", ClassName, ex.Message);
                return false;
            }

            CallForthWord("MAIN");

            // Initialize stacks and indexes
            ForthStack = (int[])GetField("ForthStack");
            ForthStackIndex = (int)GetField("ForthStackIndex");
            return true;
        }

        // ShutdownDLL - Unloads the library
        private static void ShutdownDLL()
        {
            calAssembly = null;
        }

        // CallForthWord - Calls the specified Forth word
        private static bool CallForthWord(string WordName)
        {
            var calMethodInfo = calType.GetMethod(WordName);
            if (calMethodInfo == null)
            {
                Console.WriteLine("Runtime Error: Could not find word {0}.", WordName);
                return false;
            }

            calMethodInfo.Invoke(null, null);
            return true;
        }

        // Push - pushes an integer value onto the Forth stack
        private static void Push(int val)
        {
            ForthStack[ForthStackIndex++] = val;
            var fi = calType.GetField("ForthStackIndex");
            fi.SetValue(null, ForthStackIndex);
        }

        // Pop - pops an integer value from the Forth stack
        private static int Pop()
        {
            var fi = calType.GetField("ForthStackIndex");
            fi.SetValue(null, --ForthStackIndex);
            return ForthStack[ForthStackIndex];
        }

        // Peek - peeks the value on top of stack
        private static int Peek()
        {
            return ForthStack[ForthStackIndex - 1];
        }
    }
}
