# Change Log

## Ver 1.1.6.alpha

### Changed

- Main frame size to 1000 * 750

### Added

- Preload Pane color
- Syntax detector for object.ini

## [Ver 1.1.5.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/1da7b30657e4547611dad03a39d240f90e67678a)

### Changed

- Unsafe (thread / data) change to (synchronized / concurrent)
- Parameter ArrayList to List(*generic port*)

### Added

- Rank field display and show message dialog

### Fixed

- Pause and resume cause eternally wait
- Sort pane get null value on map
- Look and feel is not working
- Sort tab unorder display and no transparency
- Button unexception enabled / disabled
- Only can run threads up to maximum of system core amount(s) simultaneously

## [Ver 1.1.4.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/ae4ffb0941f212d6c00745624ccd69ae2ef76cfe)

### Changed

- Open File to Open/Load File
- Manager initialize to initProcess
- Sort interval speed up
- GUIProcess optimized
- Threads for sorting to executor

### Added

- SQLReader for read SQL file(s)
- Custom object initial file
- Pause, Resume, skip and stop pressed icon
  
### Fixed

- Pause no response on sorting
- Sort type unset on sorting
- Arbitrary generated won't enable save as file

## [Ver 1.1.3.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/a59bc8fe5bc8c228392e2bea2626b4d05966d7e9)

### Changed

- Removed the clean cache option
  
### Added

- Cocktail sort and exchange sort
- Task menu for pause, resume and stop
- Execute worker class

### Fixed

- Color select exception when closed

## [Ver 1.1.2.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/5d095abd2239a9be174b5d326bfc1dc5600bf2c3)

### Changed

- Removed the language option
- Class Process to InitProcess

## [Ver 1.1.1.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/274051a4e0391429a58a89d25468d841455ffe2a)

### Changed

- Make the Listener package sort in kinds

### Added

- I/O Filter code pane syntax for accept
- FieldFocusListener interface
  
### Fixed

- Open and close dialog repeatedly causes memory leak

### Bug

- Memory leak on append when sorting
- Cannot run program when test mode on

## [Ver 1.1.0.alpha](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/7e7fd56421937189279845247092304ac56c464f)

### Changed

- Move jdk from 8 to 12, add external javafx(*seperated after jdk 8*) link library
- Use static link to establish local runtime environment

### Added

- Change log
- Environment for build, release and run

## [Ver 1.0.0.beta2](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/a91802030f0c82bd05a3ecb8b5a17826167e2c2d)

### Added

- Scientific notation input/output sort

## [Ver 1.0.0.beta](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/d471488684206ecdcebaaef30e485776498466d5)

- Build in jdk-8 environment