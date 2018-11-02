// <copyright file="ForthLibrary.cs" company="DeltaForth Code Samples">
// Licensed under the Creative Commons CC0 license.
// See the creative_commons_zero.markdown file in the samples directory for full license information.
// </copyright>

using System;
using System.Threading;

namespace ForthLibrary
{
    public class ForthLibrary
    {
        private static ForthExtender fe;

        static ForthLibrary()
        {
            Console.WriteLine("ForthLibrary constructor called...");
            fe = new ForthExtender("ForthLibraryTest.exe");
        }

        public static void GenerateRandomNumber(string param)
        {
            Console.WriteLine("Received parameter: " + param);
            Thread.Sleep(50);
            var RandNumber = new Random();
            var number = RandNumber.Next(50000);
            RandNumber = null;
            fe.Push(number);
        }
    }
}
