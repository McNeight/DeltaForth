/*
 * Delta Forth .NET - World's first Forth compiler for the .NET platform
 * Copyright (C)1997-2011 Valer BOCAN, PhD, Romania (valer@bocan.ro, http://www.bocan.ro/deltaforthnet)
 * 
 * This program and its source code is distributed in the hope that it will
 * be useful. No warranty of any kind is provided.
 * Please DO NOT distribute modified copies of the source code.
 * 
 */

using System;

namespace DeltaForth.DataStructures
{
    /// <summary>
    /// Definition of a local variable as used by the Forth syntactic analyzer
    /// </summary>
    public class ForthLocalVariable
    {
        /// <summary>
        /// Variable name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Word where the variable has been defined
        /// </summary>
        public string WordName { get; set; }

        /// <summary>
        /// Address of the variable (computed by the code generator)
        /// </summary>
        public int Address { get; set; }
    }
}