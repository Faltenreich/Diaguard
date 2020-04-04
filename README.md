# Diaguard

[![Release](https://img.shields.io/badge/Release-3.4.2-478063.svg)](https://play.google.com/store/apps/details?id=com.faltenreich.diaguard)
[![License](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

<img src="./resource/image/screenshot/overview.png" width="250"> 

Diaguard is an app for people with diabetes mellitus.

It replaces the handwritten diary and helps the user to quickly and easily record, evaluate and export his blood sugar and other important data as PDF or CSV. Thanks to the clearly arranged interface, the user always has an overview of his diabetes. The app also provides information on several thousand foods including carbohydrates and other nutrients.

* Quickly and easily track your blood glucose, insulin, carbohydrates, A1c, activity, weight, pulse, blood pressure and oxygen saturation
* Customizable units
* Visualize your blood glucose level in a graph
* Detailed logs of your data
* Food database with thousands of entries
* PDF and CSV export
* Backup
* Reminder functionality
* Estimated HbA1c
* Statistics
* Dark Mode

<img src="./resource/image/screenshot/entry.png" width="100"> <img src="./resource/image/screenshot/log.png" width="100">  <img src="./resource/image/screenshot/timeline.png" width="100"> <img src="./resource/image/screenshot/food.png" width="100"> <img src="./resource/image/screenshot/statistic.png" width="100"> <img src="./resource/image/screenshot/export.png" width="100"> 

<a href='https://play.google.com/store/apps/details?id=com.faltenreich.diaguard&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width="250"/></a>

## Development

#### Language

Linguistic language is English. Everything but the content localization files should be in English.

Business logc is developed in Java. Currently there are no plans to migrate it to Kotlin in order to prevent a multi-language project. A possible approach could be splitting up the monolith into multiple modules which unleashes the potential to switch language while staying consistent within a module. 

User interfaces are developed in XML and bound via [Butter Knife](http://jakewharton.github.io/butterknife).

#### Architecture

There are two architectural design patterns that are being used for Diaguard: Model-View-Controller and Domain-driven Design.

Model-View-Controller (MVC) was a widely spread design pattern in the early days of Android development until Google presented its Architecture Components in 2017 and Model-View-ViewModel (MVVM) took over. A transition to MVVM is planned due to its advantages over MVC, e.g. regarding encapsulation and testability. 

Domain-driven Design improves the structure by grouping files in features. Every feature describes one aspect of the project and should be self-contained to improve focus during development. The only exception to this rule is the shared package which contains everything that is used in multiple features (e.g. database- or networking logic).
 
#### Testing

Testing takes place via JUnit, Espresso and Robolectric. Tests are few in number and far from perfect, since most of Diaguard has been developed in the early of days of one developer. However the decision to go open source came with a code of conduct, and instrumentation testing major features was the first step in a right direction. The goal now is to unit- and instrumentation test every new and edited feature in order to increase the quality with every commit.

#### Third-party licenses

This software uses following technologies with great appreciation:

* [AndroidX](https://developer.android.com/jetpack/androidx)
* [Apache Commons Text](http://commons.apache.org/proper/commons-text)
* [BetterPickers](https://github.com/code-troopers/android-betterpickers)
* [Butter Knife](http://jakewharton.github.io/butterknife)
* [EventBus](https://github.com/greenrobot/EventBus)
* [FloatingActionButton](https://github.com/Clans/FloatingActionButton)
* [Gson](https://github.com/google/gson)
* [Joda-Time](http://www.joda.org/joda-time)
* [joda-time-android](https://github.com/dlew/joda-time-android)
* [JUnit](https://junit.org)
* [Material Components for Android](https://material.io/components)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [Opencsv](http://opencsv.sf.net)
* [Open Food Facts](http://world.openfoodfacts.org)
* [ORMLite](http://ormlite.com)
* [ORMLite Android](https://github.com/j256/ormlite-android)
* [Parallax Everywhere](https://github.com/Narfss/ParallaxEverywhere)
* [PDFjet](http://pdfjet.com)
* [Picasso](http://square.github.io/picasso)
* [Retrofit](https://square.github.io/retrofit)
* [Robolectric](http://robolectric.org)
* [Schweizer Nährwertdatenbank](http://naehrwertdaten.ch)
* [SearchView](https://github.com/lapism/SearchView)
* [Ticker](https://github.com/robinhood/ticker)

These dependencies are bundled with Diaguard but under the terms of a separate license.

## About

#### History

Development of Diaguard started in April 2014. It started as one of several small projects of one developer who was trying to gain traction on the Android platform. This project soon evolved into a vision named Diaguard which saw its initial release in July 2014. Since then it has received many updates, much appreciated feedback from its users and a little bit of marketing with the help of direct contact and mouth to mouth propaganda. But at the end of the day every line of code has been provided by one sole developer in his spare-time. This should change in April 2020 when the decision was made to go open source.

#### Business model

There is none. 

Diaguard exists for three reasons: for learning, for fun and for saying thank you. This app kickstarted the career of a professional software developer by leading through the whole software lifecycle: creating a concept, building software from scratch, publishing it to the masses and maintaining a legacy project with additions sprinkled here and there. Additionally it was and is pure fun to enhance this app and to see feedback from a helpful and thankful community. Last but not least Diaguard is a way of giving something back, back to the people who share a similar or worse fate. You are not alone.

#### Goals

Diaguard should help diabetics with monitoring their diabetes. It should be able to replace the handwritten diary completely and to supplement tasks like basal rate tests or carb counting. It should help diabetologists with monitoring their patients regularly and when preparing a health insurance application. 

Diaguard's users should always be and feel in full control. All user-generated data must stay on the same device until the user explicitely decides to transfer something to another device, e.g. during an export or backup. Nothing is shared with remote services and network is only used to read data from a food database.</br>User data must not be lost. Therefor database migrations enable user data to be carried from version 1.0 to any existing and upcoming release.

Diaguard should be accessible to most people. Therefor its design is heavily influenced by the [Material Design Guidelines](https://material.io/design) and tested with accessibility tools like [TalkBack](https://play.google.com/store/apps/details?id=com.google.android.marvin.talkback) and [Accessibility Scanner](https://play.google.com/store/apps/details?id=com.google.android.apps.accessibility.auditor).

Diaguard should only be used under the supervision of a diabetologist.</br>
Diaguard should not be used by minors without the supervision of a parent or legal guardian.</br>
Diaguard is not a medical app and serves no therapeutical purpose.

## Legal

#### Redistribution

Additionally to the permissions, conditions and limitations of the GPLv3, the permission for redistribution must be manually requested in advance. This ensures that neither the original Diaguard app or any fork will be affected negatively by terms and conditions like the [Google Play Developer Distribution Agreement](https://play.google.com/about/developer-distribution-agreement.html). If you plan to redistribute this software, please contact the maintainer at [diaguard.de@gmail.com](mailto:diaguard.de@gmail.com).

#### License

    Copyright (C) 2014-2020 Philipp Fahlteich

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

<img src="./resource/image/logo/logo_legacy.png" width="100">
