package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class controller implements Initializable {
	@FXML
	private Pane pane;
	@FXML
	private Label label;
	@FXML
	private Button play, pause,reset, prev, next;
	@FXML
	private ComboBox<String> speed;
	@FXML
	private Slider volume;
@FXML
private ProgressBar songProgress;
private File directory;
private File[] files;
private ArrayList<File> songs;
private int songNumber;
private int[] speedProgress= {25,50,75,100,125,150,175,200};
private Timer timer;
private TimerTask task;
private Boolean running;

private Media media;
private MediaPlayer mediaplayer;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		songs = new ArrayList<File>();
		directory = new File("music");
		files=directory.listFiles();
		
		if (files !=null)
		{
			for (File file : files)
			{
				songs.add(file);
System.out.println(file);
			}
		}
		media = new Media(songs.get(songNumber).toURI().toString());
		mediaplayer = new MediaPlayer(media); 
		label.setText(songs.get(songNumber).getName());
		
		for (int i=0; i < speedProgress.length; i++)
		{
			speed.getItems().add(Integer.toString(speedProgress[i] ) +"%"); 
		}
		
		speed.setOnAction(this::change);
		
	volume.valueProperty().addListener(new ChangeListener<Number>()
			{

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					// TODO Auto-generated method stub
					mediaplayer.setVolume(volume.getValue() *0.01);
				}
		
			});
	}
	
	public void playMedia()
	{
		beginTimer();
	change(null);
	mediaplayer.setVolume(volume.getValue() *0.01);
		mediaplayer.play();
	}

	public void pauseMedia()
	{
		cancelTimer();
mediaplayer.pause();		
	}
	
	public void resetMedia()
	{
	mediaplayer.seek(Duration.seconds(0.0));	
	songProgress.setProgress(0);
	
	}
	public void prevMedia()
	{
	
		if (songNumber<0)
		{
			songNumber--;
			mediaplayer.stop(); 
		/*	if (running) {
				cancelTimer();
			}*/
			mediaplayer.setVolume(volume.getValue() *0.01);
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaplayer = new MediaPlayer(media); 
			label.setText(songs.get(songNumber).getName());
			mediaplayer.play();
		}
		else {
			songNumber=songs.size()-1;
			mediaplayer.stop(); 
		/*	if (running) {
				cancelTimer();
			}*/
			mediaplayer.setVolume(volume.getValue() *0.01);
		
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaplayer = new MediaPlayer(media); 
			label.setText(songs.get(songNumber).getName());
			
			mediaplayer.play();
			
		}
	}
	public void nextMedia()
	{
		if (songNumber<songs.size()-1)
		{
			songNumber++;
			mediaplayer.stop(); 
		/*	if (running) {
				cancelTimer();
			}*/
			mediaplayer.setVolume(volume.getValue() *0.01);
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaplayer = new MediaPlayer(media); 
			label.setText(songs.get(songNumber).getName());
			mediaplayer.play();
		}
		else {
			songNumber=0;
			mediaplayer.stop(); 
			/*if (running) {
				cancelTimer();
			}*/
			mediaplayer.setVolume(volume.getValue() *0.01);
			media = new Media(songs.get(songNumber).toURI().toString());
			mediaplayer = new MediaPlayer(media); 
			label.setText(songs.get(songNumber).getName());
			mediaplayer.play();
			
		}
	}
	public void change(ActionEvent e)
	{
		if (speed.getValue()==null)
		{
			mediaplayer.setRate(1);
		}
		else
		{
		/*mediaplayer.setRate(Integer.parseInt(speed.getValue()) * 0.01);*/ //if i don't want to include % for my comboBox
		mediaplayer.setRate(Integer.parseInt(speed.getValue().substring(0,speed.getValue().length()-1)) * 0.01);
	}
	}
	public void beginTimer()
	{
		timer = new Timer();
		task = new TimerTask()
				{

					@Override
					public void run() {
						// TODO Auto-generated method stub
					running = true;
					double current = mediaplayer.getCurrentTime().toSeconds();
					double end = media.getDuration().toSeconds();
					double cyber = (current/end)*10;
					System.out.printf("\n%.1f",cyber);
					songProgress.setProgress(current/end);
					if (current/end==0)
					{
						cancelTimer();
					}
					}
			
				};
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}
	public void cancelTimer()
	{
		running=false;
		timer.cancel();
	}
}
