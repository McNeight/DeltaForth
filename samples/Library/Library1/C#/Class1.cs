// <copyright file="Class1.cs" company="DeltaForth Code Samples">
// Licensed under the Creative Commons CC0 license.
// See the creative_commons_zero.markdown file in the samples directory for full license information.
// </copyright>

using System;

namespace Library1
{
    public static class Class1
    {
        static Class1()
        {
            Console.WriteLine("This is the constructor of the static class Class1.");
        }

        public static void DisplayLogo()
        {
            Console.WriteLine("Logo typed in a C# program.");
        }
    }
}
