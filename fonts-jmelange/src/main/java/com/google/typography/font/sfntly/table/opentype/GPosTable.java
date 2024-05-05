package com.google.typography.font.sfntly.table.opentype;

import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.Table;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class GPosTable extends Table {

    private interface Offset {
        int u16majorVersion = 0;
        int u16minorVersion = 2;
        int u16scriptListOffset = 4;
        int u16featureListOffset = 6;
        int u16lookupListOffset = 8;
        int u32featureVariationsOffset = 10;
    }

    FeatureListTable feature;
    List<FeatureTable> featureList = new Vector<>();
    int majorVersion;
    int minorVersion;
    int scriptListOffset;
    int featureListOffset;
    int lookupListOffset;
    int featureVariationsOffset = -1;

    protected GPosTable(Header header, ReadableFontData data)
    {
        super(header, data);
        this.majorVersion = data.readUShort(Offset.u16majorVersion);
        this.minorVersion = data.readUShort(Offset.u16minorVersion);
        this.scriptListOffset = data.readUShort(Offset.u16scriptListOffset);
        this.featureListOffset = data.readUShort(Offset.u16featureListOffset);
        this.lookupListOffset = data.readUShort(Offset.u16lookupListOffset);
        if(this.majorVersion==1 && this.minorVersion>0)
        {
            this.featureVariationsOffset = data.readULongAsInt(Offset.u16lookupListOffset);
        }

        /* TODO -- errors in "Droid Sans Fallback" disabled for now

        this.feature = new FeatureListTable(data.slice(this.featureListOffset, this.lookupListOffset-this.featureListOffset), true);

        Iterator<FeatureTable> _it = this.feature.iterator();
        while(_it.hasNext())
        {
            this.featureList.add(_it.next());
        }

        */
    }

    public static class Builder extends Table.Builder<GPosTable>
    {

        public static Table.Builder<GPosTable> createBuilder(Header header, WritableFontData tableData)
        {
            return new Builder(header, tableData);
        }

        protected Builder(Header header, WritableFontData data) {
            super(header, data);
        }

        protected Builder(Header header, ReadableFontData data) {
            super(header, data);
        }

        protected Builder(Header header) {
            super(header);
        }

        @Override
        protected int subSerialize(WritableFontData newData) {
            return 0;
        }

        @Override
        protected boolean subReadyToSerialize() {
            return false;
        }

        @Override
        protected int subDataSizeToSerialize() {
            return 0;
        }

        @Override
        protected void subDataSet() {

        }

        @Override
        protected GPosTable subBuildTable(ReadableFontData data) {
            return new GPosTable(this.header(), data);
        }
    }
}
