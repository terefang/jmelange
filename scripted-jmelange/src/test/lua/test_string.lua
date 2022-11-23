-- len
if(string.len('123') == 3) then
print('OK')
end

if(('123'):len() == 3) then
print('OK')
end

local _s = '123'

if(_s:len() == 3) then
print('OK')
end

-- match
if(string.match('123','123') == '123') then
print('OK')
end

if(('123'):match('123') == '123') then
print('OK')
end

local _s = '123'

if(_s:match('123') == '123') then
print('OK')
end
