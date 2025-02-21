# Manual Setup
1. Install [Lein](https://leiningen.org/#install), [Java](https://adoptium.net/), [Clojure](https://github.com/casselc/clj-msi) and [Calva](https://calva.io/) if using VSCODE/IUM

## Usage
1. `lein deps`

2. `lein clean`

3. `lein compile`

4. `PORT=8080 lein run`

```
[main] INFO org.eclipse.jetty.server.Server - jetty-11.0.20; built: 2024-01-29T21:04:22.394Z; git: 922f8dc188f7011e60d0361de585fd4ac4d63064; jvm 21.0.6+7-LTS
[main] INFO org.eclipse.jetty.server.handler.ContextHandler - Started o.e.j.s.ServletContextHandler@4d746bab{/,null,AVAILABLE}
[main] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@57d9b274{HTTP/1.1, (http/1.1, h2c)}{0.0.0.0:8080}
[main] INFO org.eclipse.jetty.server.Server - Started Server@5cc447cd{STARTING}[11.0.20,sto=0] @19484ms
server running in port 8080
```

## Confirm server is running

`curl http://127.0.0.1:8080/`

## License

Copyright © 2025 Song Khoi

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
