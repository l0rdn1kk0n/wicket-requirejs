wicket-requirejs
================

Some helper to use require.js in your Apache Wicket application.

Current build status: [![Build Status](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-requirejs/badge/icon)](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-requirejs/) [![Build Status](https://travis-ci.org/l0rdn1kk0n/wicket-requirejs.png?branch=master)](https://travis-ci.org/l0rdn1kk0n/wicket-requirejs)

Dependencies
------------

* Apache Wicket 6.7.0
* wicket-webjars 0.2.0
* require.js 2.1.5

Usage
-----

You can find a simple demo application in test package: `de.agilecoders.wicket.demo`

Maven: wicket-requirejs is [available](http://search.maven.org/#artifactdetails|de.agilecoders.wicket|wicket-requirejs|0.1.0|jar) in Maven central repository.
add the following maven dependency to your pom:
<pre><code>&lt;dependency&gt;
  &lt;groupId&gt;de.agilecoders.wicket&lt;/groupId&gt;
  &lt;artifactId&gt;wicket-requirejs&lt;/artifactId&gt;
  &lt;version&gt;0.1.0&lt;/version&gt;
&lt;/dependency&gt;
</code></pre>

First, you have to install an `IRequireJsSettings` implementation to your application in `Application#init()` method:

```java
@Override
protected void init() {
    RequireJs.install(this); // use this if you want to use default settings.

    // or

    RequireJs.install(this, new MyRequireJsSettings()); // if you want to use your own configuration
}
```

Now you're able to use the `RequireJsPanel` to add all `AmdJavaScriptHeaderItem` to require.js configuration and the require.js file to
your page where you want to use require.js.

```java
public MyPage extends WebPage {
    public MyPage(PaeParameters params) {
        super(params);

        add(new RequireJsPanel("require.js"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PageB.class, "pageB.js"), "pageB"));
    }
}
```

```html
<!DOCTYPE html>
<html xmlns:wicket="http://wicket.apache.org">
<head>
    <title>Wicket - RequireJs demo application</title>
</head>
<body>
  Wicket - RequireJs integration

  <div wicket:id="requireJs"></div>

  <script type="text/javascript">
      require( ["pageB"], function(pageB) {
          pageB.demo();
      });
  </script>

    <div><a wicket:id="pageB">Page B</a></div>
</body>
</html>
```

How to get help and news
------------------------

* Keep up to date on announcements and more by following [@l0rdn1kk0n](http://twitter.com/l0rdn1kk0n) or [@mtgrigorov](http://twitter.com/mtgrigorov) on Twitter
* read more on my [blog](http://blog.agilecoders.de/).

Bug tracker
-----------

Have a bug? Please create an issue here on GitHub!

https://github.com/l0rdn1kk0n/wicket-requirejs/issues


Versioning
----------

wicket-requirejs will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.


Copyright and license
---------------------

Copyright 2012 AgileCoders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.