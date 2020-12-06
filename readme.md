# WoW Declaration Provider

A program to automatically fetch information from various sites
to generate Typescript declaration files usable for transpilers.

Since there are so many sites our there with only partial documentation
of the Addon API this project would close this gap.

The idea is to divide the problem of how to get the documentation
and generate files into the following portions:

1. **Provider**: *Providing data from a single source and put this
acquired data into a common domain structure*
2. **Reducer**: *The reducer tries to find duplicates and generate
unique declarations with all available data from each duplicate*
3. **Generator**: *The generator will render every final declaration
into a typescript declaration file based on the namespace*

The current approach is to define multiple *provider* which take a
WoW-Version (*One of `retail`, `classic`, `ptr`, `beta`*) and reduce them
to a single source of declarations.

#### Diagram
```
  Github         Gamepedia       Other Source
     \               |                /
      --------------------------------
                     |
                  Reducer
                     |
                 Generator
```

### Implemented Sources

- [Github wow-ui-source](https://github.com/Gethe/wow-ui-source/tree/live/AddOns/Blizzard_APIDocumentation)
- WIP: [WoW Gamepedia](https://wow.gamepedia.com/World_of_Warcraft_API)

### Plans

Once the Gamepedia source is fully functional the generated declaration
files will replace the existing manually created files at [wow-declarations](https://github.com/wartoshika/wow-declarations)
and [wow-classic-declarations](https://github.com/wartoshika/wow-classic-declarations).

In order so support the complex abstract structures like *Frames*, *Buttons* ...
a dedicated *Provider* may be necessary if Gamepedia does not provide this
data in a parsable format.

### How to run the program

Since this project is currently WIP you need an IDE and a
Kotlin compatible JVM runtime. I am starting the application
using the bootstrap class at `de.qhun.declaration_provider.application.DeclarationProviderApplication.kt`
using the main function `main()`.
