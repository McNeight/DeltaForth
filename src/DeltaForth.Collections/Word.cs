// <copyright file="Word.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See LICENSE.txt file in the project root for full license information.
// </copyright>

using System.Collections.Generic;

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of a word as used by the Forth syntactic analyzer.
    /// </summary>
    public class Word
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="Word"/> class.
        /// </summary>
        public Word()
        {
            this.Definition = new List<Atom>();
        }

        /// <summary>
        /// Gets or sets forth word name.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets list of atoms that define the word.
        /// </summary>
        public List<Atom> Definition { get; set; }
    }
}
