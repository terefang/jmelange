# a luaj hard-fork for selfish purposes

* since luaj 3.0.2 seems to remain static, a new evolution is needed.
* scripting under java is fun and has applications
* jme is dead

## TODO

[x] import LuaJ 3.0.2 sources from MoonlightMC/farmboy0
[x] maven-ize build system
[x] remove Java ME-isms
[x] move to luay namespace
[_] evaluate what features NIRENR fork has to offer (https://github.com/nirenr/luaj)
[_] 64-bit ints, with native bit operators
[_] array-only tables
[_] hash-only tables
[_] hack module loader to align more with classpath
[_] implement luay.luay.main.lua 5.3 std modules and changes
[_] implement luay.luay.main.lua 5.4 std modules and changes
[_] evaluate commonly packaged modules, what to implement
    [_] https://github.com/Tieske/binaryheap.luay.luay.main.lua
    [_] https://luarocks.org/modules/siffiejoe/bit32	Backport of Lua bit manipulation library introduced in 5.2
    [_] https://github.com/keplerproject/luay.luay.main.lua-compat-5.3
    [_] luay.luay.main.lua-compat53-0.7-3.el7.x86_64.rpm	Compatibility module providing Lua-5.3-style APIs for Lua 5.1
    [_] luay.luay.main.lua-cqueues-20190813-3.el7.x86_64.rpm	Stackable Continuation Queues for the Lua Programming Language
    [_] luay.luay.main.lua-cqueues-doc-20190813-3.el7.noarch.rpm	Documentation for the Stackable Continuation Queues library
    [_] luay.luay.main.lua-cyrussasl-1.1.0-1.el7.x86_64.rpm	Cyrus SASL library for Lua
    [_] luay.luay.main.lua-dbi-0.7.2-1.el7.x86_64.rpm	Database interface library for Lua
    [_] luay.luay.main.lua-event-0.4.6-1.el7.x86_64.rpm	This is a binding of libevent to Lua
    [_] luay.luay.main.lua-expat-1.4.1-1.el7.x86_64.rpm	SAX XML parser based on the Expat library
    [_] luay.luay.main.lua-fifo-0.2-1.el7.noarch.rpm	FIFO library for Lua
    [_] luay.luay.main.lua-filesystem-1.6.2-2.el7.x86_64.rpm	File System Library for the Lua Programming Language
    [_] luay.luay.main.lua-fun-0.1.3-1.el7.noarch.rpm	Functional programming library for Lua
    [_] luay.luay.main.lua-http-0.3-5.el7.noarch.rpm	HTTP library for Lua
    [_] luay.luay.main.lua-http-doc-0.3-5.el7.noarch.rpm	Documentation for HTTP library for Lua
    [_] luay.luay.main.lua-json-1.3.2-2.el7.noarch.rpm	JSON Parser/Constructor for Lua
    [_] luay.luay.main.lua-ldap-1.1.0-3.el7.x86_64.rpm	LDAP client library for Lua, using OpenLDAP
    [_] luay.luay.main.lua-lpeg-0.12-1.el7.x86_64.rpm	Parsing Expression Grammars for Lua
    [_] luay.luay.main.lua-lpeg-patterns-0.5-1.el7.noarch.rpm	A collection of LPEG patterns
    [_] luay.luay.main.lua-luaossl-20190731-1.el7.x86_64.rpm	Most comprehensive OpenSSL module in the Lua universe
    [_] luay.luay.main.lua-luaossl-doc-20190731-1.el7.noarch.rpm	Documentation for OpenSSL Lua module
    [_] luay.luay.main.lua-lunit-0.5-5.el7.noarch.rpm	Unit testing framework for Lua
    [_] luay.luay.main.lua-md5-1.1.2-1.el7.x86_64.rpm	Cryptographic Library for MD5 hashes for Lua
    [_] luay.luay.main.lua-mmdb-0.2-1.el7.noarch.rpm	MaxMind database parser for Lua
    [_] luay.luay.main.lua-mpack-1.0.4-2.el7.x86_64.rpm	Implementation of MessagePack for Lua
    [_] luay.luay.main.lua-posix-32-2.el7.x86_64.rpm	A POSIX library for Lua
    [_] luay.luay.main.lua-prelude-5.2.0-2.el7.x86_64.rpm	Lua bindings for prelude
    [_] luay.luay.main.lua-psl-0.3-2.el7.x86_64.rpm	Lua bindings to Public Suffix List library
    [_] luay.luay.main.lua-readline-3.2-1.el7.x86_64.rpm	Lua interface to the readline and history libraries
    [_] luay.luay.main.lua-sec-1.2.0-1.el7.x86_64.rpm	Lua binding for OpenSSL library
    [_] luay.luay.main.lua-socket-3.1.0-1.el7.x86_64.rpm	Network support for the Lua language
    [_] luay.luay.main.lua-term-0.03-3.el7.x86_64.rpm	Terminal functions for Lua
    [_] luay.luay.main.lua-unbound-1.0.0-1.el7.x86_64.rpm	Binding to libunbound for Lua
[_] evaluate luay.luay.main.lua-zdf, what to implement
[_] evaluate libuseful-luay.luay.main.lua, what to implement

