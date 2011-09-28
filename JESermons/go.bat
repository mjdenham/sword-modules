del *.bdt
del *.dat
del *.idx

C:\"Program Files"\CrossWire\sword-utilities-1.6.2\xml2gbs jesermons.xml

copy jesermons.bdt C:\"Documents and Settings"\denha1m\"Application Data"\Sword\modules\genbook\rawgenbook\jesermons
copy jesermons.dat C:\"Documents and Settings"\denha1m\"Application Data"\Sword\modules\genbook\rawgenbook\jesermons
copy jesermons.idx C:\"Documents and Settings"\denha1m\"Application Data"\Sword\modules\genbook\rawgenbook\jesermons
copy jesermons.conf C:\"Documents and Settings"\denha1m\"Application Data"\Sword\mods.d

copy jesermons.bdt .\module\modules\genbook\rawgenbook\jesermons
copy jesermons.dat .\module\modules\genbook\rawgenbook\jesermons
copy jesermons.idx .\module\modules\genbook\rawgenbook\jesermons
copy jesermons.conf .\module\mods.d

cd module
jar cvfM ../JESermons.zip  mods.d/jesermons.conf modules/genbook/rawgenbook/jesermons/jesermons.bdt modules/genbook/rawgenbook/jesermons/jesermons.dat modules/genbook/rawgenbook/jesermons/jesermons.idx
cd ..
rem zip a -r jesermons.zip module\*.*