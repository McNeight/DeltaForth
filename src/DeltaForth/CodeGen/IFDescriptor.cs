// <copyright file="IFDescriptor.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

using System.Reflection.Emit;

namespace DeltaForth.CodeGen
{
    /// <summary>
    /// Definition of an IF structure used to code IF-ELSE-THEN.
    /// </summary>
    public class IFDescriptor
    {
        /// <summary>
        /// Gets or sets label for the ELSE branch.
        /// </summary>
        public Label Else { get; set; }

        /// <summary>
        /// Gets or sets a value indicating whether Else has already been used.
        /// </summary>
        public bool ElseUsed { get; set; }

        /// <summary>
        /// Gets or sets label for the end of the control struct.
        /// </summary>
        public Label End { get; set; }
    }
}
