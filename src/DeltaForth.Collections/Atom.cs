// <copyright file="Atom.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011, 2018 DeltaForth Contributors. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
// </copyright>

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of an atom as used by the Forth parser
    /// </summary>
    public struct Atom
    {
        /// <summary>
        /// Gets or sets atom name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets file where the atom occured
        /// </summary>
        public string FileName { get; set; }

        /// <summary>
        /// Gets or sets line number at which the atom occured
        /// </summary>
        public int LineNumber { get; set; }
    }
}
