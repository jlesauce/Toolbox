# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

Author		: Julien LE SAUCE
Creation	: 22/02/2016
Project location : https://github.com/Awax56/Toolbox.git

Version format : MAJOR.MINOR.PATCH
Date format : YYYY-MM-DD
Tags :
	Added : for new features
	Changed : for changes in existing functionality
	Deprecated : for once-stable features removed in upcoming releases
	Removed : for deprecated features removed in the release
	Fixed : for any bug fixes
	Security : to invite users to upgrade in case of vulnerabilities


## [Unreleased]

## [1.0.2] - 2017-09-10
### Fixed
- Updated Bounce library dependency
### CHANGED
- Removed unused compiler option @SuppressWarning

## [1.0.1] - 2016-02-28
### Fixed
- ResourceManager now working on Windows (replaced "\" given by File.separator by "/" to
	access resources in sub-directories) (fixes #2)

### Changed
- Property keys are now preceded by "toolbox." to avoid overriding
- Translated Javadoc of ResourceManager.java (refs #1)


## [1.0.0] - 2016-02-22
### Added
- First project commit
- CHANGELOG.md file to track differences between each versions
- README.md file