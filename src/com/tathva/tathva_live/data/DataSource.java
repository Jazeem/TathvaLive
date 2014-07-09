package com.tathva.tathva_live.data;

import java.util.List;

import com.tathva.tathva_live.ui.Marker;

/**
 * This abstract class should be extended for new data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public abstract class DataSource {

    public abstract List<Marker> getMarkers();
}
