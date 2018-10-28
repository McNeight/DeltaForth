// <copyright file="SyntacticExceptionType.cs" company="DeltaForth Contributors">
// Copyright © 1997-2011 Valer BOCAN
// Copyright © 2018 Neil McNeight
// All rights reserved.
// Licensed under the MIT license. See the LICENSE.markdown file in the project root for full license information.
// </copyright>

namespace DeltaForth
{
    /// <summary>
    /// Types of exceptions that may be thrown by the syntactic analyzer.
    /// </summary>
    public enum SyntacticExceptionType
    {
        /// <summary>
        /// The identifier should be defined outside words.
        /// </summary>
        DeclareOutsideWords,

        /// <summary>
        /// The identifier should be defined inside words.
        /// </summary>
        DeclareInsideWords,

        /// <summary>
        /// The specified identifier is a reserved word.
        /// </summary>
        ReservedWord,

        /// <summary>
        /// The specified identifier is invalid.
        /// </summary>
        InvalidIdentifier,

        /// <summary>
        /// Unable to define constant since the stack is empty.
        /// </summary>
        UnableToDefineConst,

        /// <summary>
        /// Unable to alloc variable space since the stack is empty.
        /// </summary>
        UnableToAllocVar,

        /// <summary>
        /// An unexpected end of file has occured.
        /// </summary>
        UnexpectedEndOfFile,

        /// <summary>
        /// A constant of type other than 'int' was specified for ALLOT.
        /// </summary>
        WrongAllotConstType,

        /// <summary>
        /// A word definition occured within another word definition.
        /// </summary>
        NestedWordsNotAllowed,

        /// <summary>
        /// Malformed BEGIN-WHILE-REPEAT struct encountered.
        /// </summary>
        MalformedBWRStruct,

        /// <summary>
        /// Malformed BEGIN-AGAIN struct encountered
        /// </summary>
        MalformedBAStruct,

        /// <summary>
        /// Malformed BEGIN-UNTIL struct encountered
        /// </summary>
        MalformedBUStruct,

        /// <summary>
        /// Malformed IF-ELSE-THEN struct encountered
        /// </summary>
        MalformedIETStruct,

        /// <summary>
        /// Malformed DO-LOOP/+LOOP struct encountered
        /// </summary>
        MalformedDLStruct,

        /// <summary>
        /// Malformed CASE-OF-ENDCASE struct encountered
        /// </summary>
        MalformedCOEStruct,

        /// <summary>
        /// Control structures must be terminated before ';'.
        /// </summary>
        UnfinishedControlStruct,

        /// <summary>
        /// Malformed conversion.
        /// </summary>
        MalformedConversion,

        /// <summary>
        /// Conversions must be finished before ';'.
        /// </summary>
        UnfinishedConversion,

        /// <summary>
        /// Word MAIN not defined.
        /// </summary>
        MainNotDefined,

        /// <summary>
        /// Duplicate constant defined.
        /// </summary>
        DuplicateConst,

        /// <summary>
        /// Duplicate variable defined.
        /// </summary>
        DuplicateVar,
    }
}
