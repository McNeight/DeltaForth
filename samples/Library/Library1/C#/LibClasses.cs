using System;

namespace Library1
{
	/// <summary>
	/// Summary description for Class1.
	/// </summary>
	public class Class1
	{
		static Class1()
		{
			Console.WriteLine("This is the constructor of the Class1 class.");
		}

		public static void DisplayLogo()
		{
			Console.WriteLine("Logo typed in a C# program.");
		}
	}

	public class Class2
	{
		static Class2()
		{
			Console.WriteLine("This is the constructor of the Class2 class.");
		}

		public static void DisplayRandom()
		{
			Random RandNumber = new Random();
			Console.WriteLine("Random number: {0}", RandNumber.Next(1000));
			RandNumber = null;
		}
	}
}
