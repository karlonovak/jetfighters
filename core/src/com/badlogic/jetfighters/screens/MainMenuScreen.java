package com.badlogic.jetfighters.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.jetfighters.JetFightersGame;
import com.badlogic.jetfighters.client.eventbus.JoinGameMessageResponseListener;

import java.util.Random;

public class MainMenuScreen implements Screen {

    private final JetFightersGame game;
    private final OrthographicCamera camera = new OrthographicCamera();;

    public String jetId;
    public boolean jetIdPicked = false;
    private Texture pilotTexture = new Texture(Gdx.files.internal("jet_pilot.png"));

    private int attemptNumber = 1;
    public long lastConnectionAttemptTimestamp;

    private Random random = new Random();

    public MainMenuScreen(final JetFightersGame game) {
        JetFightersGame.eventBus.register(new JoinGameMessageResponseListener(game));
        this.game = game;
        this.jetId = "Player" + random.nextInt();
        this.camera.setToOrtho(false, 1024, 768);
    }

    @Override
    public void show() {
        JetIdInputListener listener = new JetIdInputListener(this);
        Gdx.input.getTextInput(listener, "Enter your Jet Name:", jetId, "");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(pilotTexture, 162, 100);
        game.font.draw(game.batch, "Welcome to JetFighters!", 100, 150);
        if (jetIdPicked) {
            game.font.draw(game.batch, "Entering game as " + jetId, 100, 100);
            game.font.draw(game.batch, "Acquiring connection to server... [attempt " + attemptNumber + "]", 100, 80);
            tryToReconnectToServer();
        }
        game.batch.end();
    }

    private void tryToReconnectToServer() {
        if (System.currentTimeMillis() - lastConnectionAttemptTimestamp > 5000) {
            game.client.joinGame(jetId);
            this.attemptNumber++;
            this.lastConnectionAttemptTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}