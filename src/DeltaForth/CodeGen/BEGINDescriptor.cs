// <copyright file="BEGINDescriptor.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

using System.Reflection.Emit;

namespace DeltaForth.CodeGen
{
    /// <summary>
    /// Definition of a structure used to code BEGIN-AGAIN, BEGIN-UNTIL, BEGIN-WHILE-REPEAT.
    /// </summary>
    public class BEGINDescriptor
    {
        /// <summary>
        /// Gets or sets label for BEGIN.
        /// </summary>
        public Label Begin { get; set; }

        /// <summary>
        /// Gets or sets label for END.
        /// </summary>
        public Label End { get; set; }
    }
}
