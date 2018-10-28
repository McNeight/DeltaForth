// <copyright file="ExternalWord.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of an external word as used by the Forth syntactic analyzer.
    /// </summary>
    public struct ExternalWord
    {
        /// <summary>
        /// Gets or sets word name.
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets library filename.
        /// </summary>
        public string Library { get; set; }

        /// <summary>
        /// Gets or sets class name.
        /// </summary>
        public string Class { get; set; }

        /// <summary>
        /// Gets or sets method name.
        /// </summary>
        public string Method { get; set; }
    }
}
