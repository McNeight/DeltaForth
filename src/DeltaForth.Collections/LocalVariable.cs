// <copyright file="LocalVariable.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011, 2018 DeltaForth Contributors. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
// </copyright>

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of a local variable as used by the Forth syntactic analyzer
    /// </summary>
    public struct LocalVariable
    {
        /// <summary>
        /// Gets or sets variable name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets word where the variable has been defined
        /// </summary>
        public Word Word { get; set; }

        /// <summary>
        /// Gets or sets address of the variable (computed by the code generator)
        /// </summary>
        public int Address { get; set; }
    }
}
