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
using System.Collections;
using System.Reflection;
using System.Reflection.Emit;
using System.Threading;
using System.IO;
using DeltaForth.DataStructures;

namespace DeltaForth.CodeGen
{
    /// <summary>
    /// Definition of a structure used to code BEGIN-AGAIN, BEGIN-UNTIL, BEGIN-WHILE-REPEAT
    /// </summary>
    public class BEGINDescriptor
	{
        /// <summary>
        /// Label for BEGIN
        /// </summary>
        public Label lbBegin { get; set; }
        
        /// <summary>
        /// Label for END
        /// </summary>
        public Label lbEnd { get; set; }
	}
}
