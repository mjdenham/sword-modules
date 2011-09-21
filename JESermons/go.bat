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

jar cvfM jesermons.zip -C module .
rem zip a -r jesermons.zip module\*.*