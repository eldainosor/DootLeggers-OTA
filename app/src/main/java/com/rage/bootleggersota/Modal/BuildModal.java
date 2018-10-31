package com.rage.bootleggersota.Modal;

public class BuildModal {

    private String title, description, size, filename;
    private String breakText;
    private boolean isBreakText;
    private int colorBreakText;
    private String downloadLink;

    public BuildModal(boolean isBreakText, String title, String description, String size, String downloadLink, String filename) {
        this.isBreakText = isBreakText;
        this.title = title;
        this.description = description;
        this.size = size;
        this.downloadLink = downloadLink;
        this.filename = filename;
    }

    public BuildModal(boolean isBreakText, String breakText, int colorBreakText) {
        this.isBreakText = isBreakText;
        this.breakText = breakText;
        this.colorBreakText = colorBreakText;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSize() {
        return size;
    }

    public String getBreakText() {
        return breakText;
    }

    public boolean isBreakText() {
        return isBreakText;
    }

    public int getColorBreakText() {
        return colorBreakText;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getFilename() {
        return filename;
    }
}
