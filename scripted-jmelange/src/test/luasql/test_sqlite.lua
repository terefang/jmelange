-- sqlite

local _dao = luasql_sqlite('test.sqlite.db', 'sa', '');

local _res = _dao:query('SELECT 1 AS clocal');

print(_res);
print(_res[1]);
print(_res[1].clocal);

_dao:close();
