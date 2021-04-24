package org.devlion.appupdate;

public interface OnEvent {
    void onPreExecute();
    void onProgressUpdate(int count);
    void onPostExecute();
}
