/*
Copyright 2024 LinuxSuRen.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package typing.linuxsuren.github.io.service;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultAudioService implements AudioService {
    private String cacheDir;

    public DefaultAudioService() {
        String dir = System.getProperty("user.home") + File.separator + ".config/typing/audio";
        File cacheFile = new File(dir);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        this.cacheDir = cacheFile.getAbsolutePath();
    }

    @Override
    public void play(String word) {
        File mp3File = getMp3File(word);
        if (!mp3File.exists() || mp3File.length() <= 0) {
            return;
        }

        try (InputStream input = new FileInputStream(mp3File)) {
            Player playMP3 = new Player(input);
            playMP3.play();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveCache(String word) {
        File mp3File = getMp3File(word);
        if (mp3File.exists() && mp3File.length() > 0) {
            return;
        }

        try (OutputStream out = new FileOutputStream(mp3File)) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://cdn.yourdictionary.com/audio/en/" + mp3File.getName()))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 304) {
                out.write(response.body().getBytes());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (mp3File.length() == 0) {
                mp3File.delete();
            }
        }
    }

    private File getMp3File(String word) {
        String mp3FileName = word + ".mp3";
        return new File(cacheDir, mp3FileName);
    }

    @Override
    public void saveCacheAsync(String word) {
        new Thread(() -> saveCache(word)).start();
    }

    @Override
    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }
}
