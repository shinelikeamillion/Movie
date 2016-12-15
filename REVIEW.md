### review - 14/12/2016
1. user the tools attributes instead of the android attributes when in design phase
2. do not hardcoding my string
3. learn more RecycleView 
[benefits](https://guides.codepath.com/android/using-the-recyclerview)
[youtube](https://www.youtube.com/watch?v=LqBlYJTfLP4)
4. Loader is wonderful 
[Reto said something](https://classroom.udacity.com/nanodegrees/nd801/parts/8011345403/modules/432468910275460/lessons/3681658545/concepts/15935287200923)
5. always considering user rotates the device
6. filter out corresponding movie list based one the user's selection
[learn more here](https://developer.android.com/reference/android/content/CursorLoader.html#CursorLoader
[and here](https://developer.android.com/reference/android/content/ContentResolver.html#query
``` java
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "onCreateLoader");
        if (// sort criteria is "most popular") {
            return new CursorLoader(
                    getActivity(),
                    uri,
                    null,
                    SELECTION_MOST_POPULAR,
                    SELECTION_ARGS_MOST_POPULAR,
                    Utility.getPreferredSortOrder(getActivity()) + " DESC"
            );
        } else if (// sort criteria is "top rated") {
            return new CursorLoader(
                    getActivity(),
                    uri,
                    null,
                    SELECTION_TOP_RATED,
                    SELECTION_ARGS_TOP_RATED,
                    Utility.getPreferredSortOrder(getActivity()) + " DESC"
            );
        }
```
7. learn about permissions at 
[here](https://developer.android.com/training/articles/security-tips.html#Permissions)
[here](https://classroom.udacity.com/nanodegrees/nd801/parts/8011345402/modules/425665870775460/lessons/1469948762/concepts/52166286600923#)
[and here](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-runtime-permissions)
8. parent activity at minimum sdk version
[learn more](https://developer.android.com/training/appbar/up-action.html#declare-parent)
9. learn to use HTTPURLConnection Volley OkHttp or Gson
 [learn more here](https://medium.com/android-news/android-networking-i-okhttp-volley-and-gson-72004efff196#.lslh887a1)
 [and here](https://packetzoom.com/blog/which-android-http-library-to-use.html)
10. considering the tablet
[check out here](The official Android Developer Site)
[this simple tutorial](http://www.vogella.com/tutorials/AndroidFragments/article.html)
11. Smaller projects are easier to maintain. 项目越小越容易维护
12. give [Butter Knife](http://jakewharton.github.io/butterknife/) or [DataBing](https://medium.com/google-developers/no-more-findviewbyid-457457644885#.sd5kwgre3) Library a try
[learn more](http://www.thekeyconsultant.com/2013/09/5-reasons-you-should-use-butterknife.html)
13. using an implicit intent(隐式意图) to startActivity, it is good practice to do so.
learn more about it [here](https://developer.android.com/guide/components/intents-filters.html#ExampleSend), [here](https://developer.android.com/training/basics/intents/sending.html#Verify) [and here](https://www.youtube.com/watch?v=HGElAW224dE)

### review - 16/12/2016
1. An emptyView is needed. 
2. Test more before submit

TODO:
>
1. 缓存图片到本地
2. 动态计算图片的宽高
3. redesign the ui ( no connection and no data)

> Options:
1. more material design when i have more free time
2. let users can share
3. keep database fresh
4. international and accessible
