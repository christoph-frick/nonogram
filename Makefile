release:
	lein do clean, cljsbuild once min, garden once
	rsync -av --delete resources/public/ docs/
