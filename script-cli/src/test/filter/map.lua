print(stringify(_IN),stringify(_OUT));

for _k, _v in pairs(_IN) do
    _OUT[_k] = _v
end

_OUT["NEW"] = "nx"

return _OUT
-- return false