# Diaguard

![Version](https://img.shields.io/badge/Version-3.4.2-blue)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat)

*Android app for diabetics*

## Description

Diaguard is a free Android app without ads for people with Diabetes mellitus.

It replaces the handwritten diary and helps its users to keep track of their blood glucose and other medical data. Those information are displayed well-arranged and can be exported as PDF or CSV. Additionally Diaguard offers a food database with thousands of products to determine their carbohydrates and other nutriments.

Features

• Quickly and easily track your blood glucose, insulin, carbohydrates, A1c, activity, weight, pulse, blood pressure and oxygen saturation
• Customizable units
• Visualize your blood glucose level in a graph
• Detailed logs of your data
• Food database with thousands of entries
• PDF and CSV export
• Backup
• Reminder functionality
• Estimated HbA1c
• Statistics
• Dark Mode

## Development

### History

Development of Diaguard started in April 2014. Philipp Fahlteich, then working on his Bachelor of Science, started several small projects in order to harden his skills in developing for the Android platform. One of these projects evolved into a vision named Diaguard which saw its initial release in July 2014. Since then Diaguard has seen many updates, much appreciated feedback from its users and a little bit of marketing with the help of direct contact and mouth to mouth propaganda. But at the end of the day every line of code has been provided by one sole developer in his spare-time. This should change in April 2020 when the decision was made to go open source.

### Language

Diaguard is developed in Java. Currently there are no plans to migrate it to Kotlin in order to prevent a multi-language project. A possible approach could be splitting up the monolith into multiple modules which unleashes the potential to switch language while staying consistent within a module. 

User interfaces are designed in XML. Currently there are no plans to switch to a declarative framework like Jetpack Compose as it is still under heavy development.

### Architecture

There are two architectural design patterns that are being used for Diaguard: Model-View-Controller and Domain-driven Design.

Model-View-Controller (MVC) was a widely spread design pattern in the early days of Android development until Google presented its Architecture Components in 2017 and Model-View-ViewModel (MVVM) took over. A transition to MVVM is planned due to its advantages over MVC, e.g. regarding encapsulation and testability. 

Domain-driven Design improves the structure by grouping files in features. Every feature describes one aspect of the project and should be self-contained in order to improve focus during development. The only exception to this rule is the shared package which contains everything that is used in multiple features (e.g. database- or networking logic).
 
### Testing

Testing takes place via JUnit 4, Espresso and Robolectric. Since the app has been mainly developed by a junior developer during studies, testing is far from perfect. Nonetheless the goal is to unit- and instrumentation test every feature to increase the code coverage with every commit.

## License

Copyright 2020 Philipp Fahlteich