using System;

namespace Library1
{
	/// <summary>
	/// Summary description for Class1.
	/// </summary>
	public class Class1
	{
		public Class1()
		{
			//
			// TODO: Add constructor logic here
			//
		}

		public static void DisplayLogo()
		{
			Console.WriteLine("Logo typed in a C# program.");
		}
	}

	/// <summary>
	/// Summary description for Class1.
	/// </summary>
	public class Class2
	{
		public Class2()
		{
			//
			// TODO: Add constructor logic here
			//
		}

		public static void DisplayRandom()
		{
			Random RandNumber = new Random();
			Console.WriteLine("Random number: {0}", RandNumber.Next(1000));
			RandNumber = null;
		}
	}
}
