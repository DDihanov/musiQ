# DEPRECATED
I don't consider to update this any time soon, so it's just one for the history books.

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

<b>The project was in need of some refactoring since I created it when I was new to developing Android apps years ago</b>, therefore I compiled a list with TODOs and will gradually strike them out when as I complete them:
- ~~Refactored MVP logic to be more precice with the design pattern~~
- ~~Refactored app backend to use repositories~~
- ~~Refactored app backend to use UseCases~~
- ~~Migrated to AndroidX~~
- ~~Updated Material design/libraries~~
- ~~Refacted static classes to be injected instead~~
- ~~Genral QOL code refactoring~~
- ~~Fixed last.fm image fetching(since they disabled it) - artist images are now fetched from a different API~~
- Completeley remove the few static helper methods that are left in the UI
- Single activity architecture with Android Navigation Component
- Rewrite in Kotlin

# musiQ
a musical encyclopedia

<a href='https://play.google.com/store/apps/details?id=com.dihanov.musiq&utm_source=github&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="300"/></a>

### Special thanks to https://github.com/gabynnette for the design of the app icons, logos and Google Play Store banner, much appreciated!

This application is designed using the MVP pattern.
The application features a beautiful material design, and makes use of cutting-edge libraries to function, e.g.:
<ul>
    <li>RxJava2</li>
    <li>Dagger2</li>
    <li>Retrofit2</li>
    <li>RxBinding</li>
    <li>ButterKnife</li>
    <li>OkHttp</li>
    <li>Gson</li>
    <li>Glide</li>
    <li>DBFlow</li>
    <li>Robolectric</li>
    <li>PowerMock</>
    <li>Mockito</>
    <li>LeakCanary</>
</ul>

The API for all the information is the LastFM API.

Other libraries include custom labels, tooltips, Calligraphy library for fonts,
and Google's support libraries.

This application is meant to be a musical library for anyone who might enjoy having a cool app, which fetches information
fast and accurate from a reliable musical site, with up-to-date information about artists, albums and songs.
The program is very versatile, because it makes use of all the features the last.fm API provides such as top artists, top songs,
top songs per artist, top artists by geographical region etc...

Recently I added scrobbling integration, so the application now scrobbles to last.fm from music players like:
<ul>
	<li>YouTube</li>
    <li>Google Play Music</li>
    <li>Spotify</li>
    <li>Pandora</li>
    <li>SoundCloud</li>
    <li>VLC</li>
    <li>and more...</li>
</ul>

Some features this app. supports:
- Search for artists and albums
- Add artists and albums to your favorites for quicker access
- Select a specific artists to view more detailed information about them like their genres, biography and albums
- Select a specific album to view detailed information about it.
- Different phones support different emojis - you will have an unique penguin mascot for your device!
- Scrobbling notifications
- Currently scrobbling track information
- Scrobble history
- 'Love track' button
- Scrobbling from cache(e.g. offline scrobbles to be scrobbled at a later point)
- Beautiful graph showing the top tracks and top artists in a time span, which the user can specify


Features to be implemented will be:
- Track statistics in the Artist detail menu
- More user-specific information from last.fm

If you have any recommendations feel free to email me.
</br>
NOTE: If you fork/clone this repo, be sure to add the following to your gradle.properties:

</br>
API_KEY = "YOUR_LASTFM_API_KEY"
</br>
API_SECRET = "YOUR_LASTFM_API_SECRET"
</br>

