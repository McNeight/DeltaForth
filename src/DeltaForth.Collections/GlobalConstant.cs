// <copyright file="GlobalConstant.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011, 2018 DeltaForth Contributors. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
// </copyright>

namespace DeltaForth.Collections
{
    /// <summary>
    /// Definition of a global constant as used by the Forth syntactic analyzer
    /// </summary>
    public struct GlobalConstant
    {
        /// <summary>
        /// Gets or sets constant name
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets constant value (can be either string or integer)
        /// </summary>
        public object Value { get; set; }
    }
}
