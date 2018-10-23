# Changelog
All notable changes to DeltaForth will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Added default .editorconfig file from Visual Studio 2017.
- Added latest .gitignore from https://github.com/github/gitignore/blob/master/VisualStudio.gitignore
- Created new DeltaForth.Collections library

### Changed
- Moved source into src/DeltaForth to reflect namespace structure.

### Fixed
### Removed
- Deleted all binaries (*.dll and *.exe) from repository.
- Removed docs/whatsnew.txt & replaced with this changelog.

## [1.4] - 2011-11-20
### Changed
- While still a console application, colors have been added for greater screen readability.
- Refactored compiler code to bring it to the .NET 4 world.

### Fixed
- Fixed bug described in article KB131-01.

## [1.3.1] - 2006-01-21
### Fixed
- Fixed bug described in article KB13-01.

## [1.3] - 2004-09-25
### Added
- Added words I' and J.
- Added the Dots sample.

### Changed
- Modified the Library1 sample so that the C# constructor is called.
- Documentation is back in the PDF format.

### Fixed
- Fixed bugs described in articles KB12-01 and KB12-02.

## [1.2] - 2003-05-12
### Added
- Added the Hanoi.4th sample program.

### Changed
- Compiled Forth modules may now be strongly signed (see the /KEY option).
- Delta Forth documentation now comes in the popular RTF format.
- Changed the install program (I now use the Nullsoft Install System).

## [1.1] - 2002-05-07
### Fixed
- Fixed bugs described in articles KB10-01 & KB10-02.

## [1.0] - 2002-01-19
### Added
- Added the /FS, /RS and /MAP command-line options.
- Added enhanced library samples.

### Fixed
- Fixed bugs described in articles KB10B2A-01 & KB10B2A-02.
- Corrected a few typos in the documentation.

## 1.0 beta 2a - 2001-12-10
### Fixed
- Fixed bug described in article KB10B2-01.

## [1.0 beta 2] - 2001-11-14
### Added
- Added the ability to call external words.

### Changed
- The samples are now pre-compiled, so you can see the effect without bothering about the compiler itself.

### Fixed
- Fixed bugs described in articles KB10B1-01, KB10B1-02, KB10B1-03.

## 1.0 beta 1 - 2001-10-25
### Changes over the Java version (now retired)
1. Comments do not require space after ( or \ or before )
2. The text display operator does not require a space after ."
3. Constants can be either strings or integers
4. DUMP primitive changed to "<text>" or string variable
5. When declaring local variables, use VARIABLE instead of LOCAL
6. Traditional Forth conversion words changed to int2str and str2int
7. As of now, there's no ?TERMINAL (query-terminal) primitive implemented
8. Identifiers should not begin with a figure and be less than 31 chars long
9. The "fetch" primitive is now @ (was C)

## [1.0-java] - 2000-11-06
### Added
- First non-alpha release.
- Source code freely available.

## 0.95-java - 2000-01-30
### Added
- Added command line parameters.
- Added support for local variables.
- Bundled the Algebraic to RPN/Forth Converter.

## [0.8-java] - 1999-03-04
### Added
- Added the DUMP primitive.
- Added support for external libraries.
- Added the COUNT primitive.
- Added the <# #> conversion words.
- Added the EXIT primitive.

## [0.5-java] - 1998-08-24
## First Public Release

[Unreleased]: https://github.com/McNeight/DeltaForth/compare/v1.4...HEAD
[1.4]: https://github.com/McNeight/DeltaForth/compare/v1.3.1...v1.4
[1.3.1]: https://github.com/McNeight/DeltaForth/compare/v1.3...v1.3.1
[1.3]: https://github.com/McNeight/DeltaForth/compare/v1.2...v1.3
[1.2]: https://github.com/McNeight/DeltaForth/compare/v1.1...v1.2
[1.1]: https://github.com/McNeight/DeltaForth/compare/v1.0...v1.1
[1.0]: https://github.com/McNeight/DeltaForth/compare/v1.0b2...v1.0
[1.0 beta 2]: https://github.com/McNeight/DeltaForth/compare/v1.0-java...v1.0b2
[1.0-java]: https://github.com/McNeight/DeltaForth/compare/v0.8-java...v1.0-java
[0.8-java]: https://github.com/McNeight/DeltaForth/compare/v0.5-java...v0.8-java
[0.5-java]: https://github.com/McNeight/DeltaForth/tree/v0.5-java