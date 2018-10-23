// <copyright file="GlobalVariable.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011, 2018 DeltaForth Contributors. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
// </copyright>

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of a global variable as used by the Forth syntactic analyzer
    /// </summary>
    public struct GlobalVariable
    {
        /// <summary>
        /// Gets or sets variable name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets number of cells required by the variable
        /// </summary>
        public int Size { get; set; }

        /// <summary>
        /// Gets or sets address of the variable (computed by the code generator)
        /// </summary>
        public int Address { get; set; }
    }
}
