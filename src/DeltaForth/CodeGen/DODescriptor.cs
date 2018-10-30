// <copyright file="DODescriptor.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

using System.Reflection.Emit;

namespace DeltaForth.CodeGen
{
    /// <summary>
    /// Definition of a structure used to code DO-LOOP/+LOOP structure.
    /// </summary>
    public class DODescriptor
    {
        /// <summary>
        /// Gets or sets label for DO.
        /// </summary>
        public Label Do { get; set; }

        /// <summary>
        /// Gets or sets label for LOOP.
        /// </summary>
        public Label Loop { get; set; }
    }
}
