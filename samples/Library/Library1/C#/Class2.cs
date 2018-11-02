// <copyright file="Class2.cs" company="DeltaForth Code Samples">
// Licensed under the Creative Commons CC0 license.
// See the creative_commons_zero.markdown file in the samples directory for full license information.
// </copyright>

using System;

namespace Library1
{
    public class Class2
    {
        public Class2()
        {
            Console.WriteLine("This is the constructor of the Class2 class.");
        }

        public void DisplayRandom()
        {
            var RandNumber = new Random();
            Console.WriteLine("Random number: {0}", RandNumber.Next(1000));
            RandNumber = null;
        }
    }
}
