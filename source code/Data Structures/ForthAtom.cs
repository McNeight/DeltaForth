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
    /// Definition of an atom as used by the Forth parser 
    /// </summary>
    public class ForthAtom
    {
        /// <summary>
        /// Atom name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// File where the atom occured
        /// </summary>
        public string FileName { get; set; }

        /// <summary>
        /// Line number at which the atom occured
        /// </summary>
        public int LineNumber { get; set; }
    }
}