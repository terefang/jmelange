package com.github.terefang.jmelange.swing.widgets.voops;

import com.github.terefang.jmelange.commons.lang.Executable;
import com.github.terefang.jmelange.swing.SwingHelper;
import org.jetbrains.annotations.NotNull;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MiniColorChooserDialog extends JFrame
{
    JPanel okCancel = SwingHelper.createHxBox();
    MiniColorChooser chooser;
    
    Executable<Color> _ok;
    Runnable _cancel;
    
    public MiniColorChooserDialog()
    {
        super("Select Color ...");
        this.setPreferredSize(new Dimension(300,300));
        JPanel pane = SwingHelper.createVxBox();
        this.chooser = new MiniColorChooser();
        pane.add(this.chooser);
        
        okCancel.add(SwingHelper.createButton("Select", ()->{
            this.setVisible(false);
            if(_ok!=null)
            {
                _ok.execute(this.chooser.getColor());
            }
        }));

        okCancel.add(SwingHelper.createButton("Cancel", ()->{
            this.setVisible(false);
            if(_cancel!=null)
            {
                _cancel.run();
            }
        }));

        pane.add(okCancel);
        this.add(pane);
        this.pack();
    }
    
    public void setOkListener(Executable<Color> __ok)
    {
        _ok = __ok;
    }
    
    public void setCancelListener(Runnable __cancel)
    {
        _cancel = __cancel;
    }
    
    public void show(Executable<Color> __ok, Runnable __cancel)
    {
        setOkListener(__ok);
        setCancelListener(__cancel);
        super.setVisible(true);
    }
    
    public void show(Executable<Color> __ok)
    {
        setOkListener(__ok);
        setCancelListener(null);
        super.setVisible(true);
    }
    
   
    public Future<Color> showFuture()
    {
        MiniColorChooserFuture _future = new MiniColorChooserFuture();
        this.setOkListener((_c)->{
            _future.value = _c;
            _future.done = true;
        });
        this.setCancelListener(()->{
            _future.value = null;
            _future.done = true;
        });
        super.setVisible(true);
        return _future;
    }
    
    class MiniColorChooserFuture implements Future<Color>
    {
        public Color value;
        public boolean done;
        @Override
        public boolean cancel(boolean mayInterruptIfRunning)
        {
            return false;
        }
        
        @Override
        public boolean isCancelled()
        {
            return false;
        }
        
        @Override
        public boolean isDone()
        {
            return this.done;
        }
        
        @Override
        public Color get() throws InterruptedException, ExecutionException
        {
            while(!this.done)
            {
                Thread.sleep(1000L);
            }
            return this.value;
        }
        
        @Override
        public Color get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
        {
            while(!this.done && timeout>0)
            {
                timeout = timeout>>1;
                Thread.sleep(unit.toMillis(Math.max(timeout,1)));
            }
            if(!this.done)
            {
                throw new TimeoutException();
            }
            return this.value;
        }
    }
}
