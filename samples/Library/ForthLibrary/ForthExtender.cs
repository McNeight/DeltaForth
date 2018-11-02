// <copyright file="ForthExtender.cs" company="DeltaForth Code Samples">
// Licensed under the Creative Commons CC0 license.
// See the creative_commons_zero.markdown file in the samples directory for full license information.
// </copyright>

using System;
using System.Reflection;

namespace ForthLibrary
{
    /// <summary>
    /// Provides the basic stack operations and word calling for an associated Delta Forth .NET program.
    /// </summary>
    public class ForthExtender
    {
        /// <summary>
        /// Delta Forth .NET Assembly.
        /// </summary>
        private readonly Assembly calAssembly;

        /// <summary>
        /// Delta Forth .NET Type.
        /// </summary>
        private readonly Type calType;

        private readonly int[] ForthStack;
        private int ForthStackIndex;

        /// <summary>
        /// Initializes a new instance of the <see cref="ForthExtender"/> class.
        /// Initializes the assembly and the stacks, including stack pointers.
        /// </summary>
        /// <param name="FileName">File name containing the Delta Forth .NET program.</param>
        public ForthExtender(string FileName)
        {
            // Initialize assembly and type
            this.calAssembly = Assembly.LoadFrom(FileName);
            this.calType = this.calAssembly.GetType("DeltaForthEngine", true, true);

            // Initialize Forth stack
            this.ForthStack = (int[])this.GetField("ForthStack");
            this.ForthStackIndex = (int)this.GetField("ForthStackIndex");
        }
        #region "Forth Stack Handling Methods"

        /// <summary>
        /// Pushes an integer value onto the Forth stack.
        /// </summary>
        /// <param name="val">Value to push.</param>
        public void Push(int val)
        {
            this.ForthStack[this.ForthStackIndex++] = val;
            var fi = this.calType.GetField("ForthStackIndex");
            fi.SetValue(null, this.ForthStackIndex);
        }

        /// <summary>
        /// Pops an integer value from the Forth stack.
        /// </summary>
        /// <returns>Value retrieved from the stack.</returns>
        public int Pop()
        {
            var fi = this.calType.GetField("ForthStackIndex");
            fi.SetValue(null, --this.ForthStackIndex);
            return this.ForthStack[this.ForthStackIndex];
        }

        /// <summary>
        /// Peeks the value on top of the Forth stack.
        /// </summary>
        /// <returns>Value retrieved from the stack.</returns>
        public int Peek()
        {
            return this.ForthStack[this.ForthStackIndex - 1];
        }

        #endregion

        /// <summary>
        /// Returns the object associated with the specified field (Delta Forth .NET variable).
        /// </summary>
        /// <param name="FieldName">Field name.</param>
        /// <returns>Object associated with the field.</returns>
        private object GetField(string FieldName)
        {
            var fi = this.calType.GetField(FieldName);
            if (fi == null)
            {
                return null;
            }

            return fi.GetValue(null);
        }

        /// <summary>
        /// Calls a specified Delta Forth .NET word.
        /// </summary>
        /// <param name="WordName">Word name.</param>
        /// <returns>TRUE if the call is successful.</returns>
        private bool CallForthWord(string WordName)
        {
            var calMethodInfo = this.calType.GetMethod(WordName.ToUpper());
            if (calMethodInfo == null)
            {
                return false;
            }

            calMethodInfo.Invoke(null, null);
            return true;
        }
    }
}
