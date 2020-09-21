# GDStatistics

[![Build Status](https://travis-ci.org/DoubleCookies/GDStatistics.svg?branch=master)](https://travis-ci.org/DoubleCookies/GDStatistics)

Tool for collecting statistics about Geometry Dash featured levels. Based on Alex1304 [ultimategdbot](https://github.com/alex1304/ultimategdbot) project.

## Features
- Collect information about featured and epic levels.
- Split statistics by difficulties.
- Additional lists with info about soundtracks, descriptions, level creators, etc.
- Little bonus: top-50 most popular demons.

## Structure
- **Statistics** folder contains a lot of markdown lists with different statistics information. All lists are generated in Markdown (that's why you can see information directly on GitHub).
   - Root folder contain lists for all difficulties and lists for soundtracks, builders, etc.
   - Subfolders contain lists for difficulties (featured and epic levels stats only).
- **src** folder contains sources for this project.

## Plans
- Multithreading (probably)
- Create project github page
- Describe how project works
- Create some illustrations

## License
This project has [MIT License](https://opensource.org/licenses/MIT)
