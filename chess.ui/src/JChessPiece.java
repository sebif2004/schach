import chess.core.ChessPieceId;
import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JChessPiece extends JPanel
{

    private final JSVGCanvas svg;



    private void updateSvg()
    {
        svg.setSize(new Dimension(getWidth(), getHeight()));
        svg.setLocation(0, 0);
        svg.revalidate();

        repaint();
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        int horizontalWidth = (int) (getHeight() * 0.05); // Breite des horizontalen Randes
        int verticalWidth = (int) (getWidth() * 0.05); // Breite des vertikalen Randes

        int horizontalLength = ((int) (getWidth() * 0.2)); // Länge des horizontalen Randes
        int verticalLength = ((int) (getHeight() * 0.2)); // Länge des vertikalen Randes

        int xPadding = (int) (getWidth() * 0.05);
        int yPadding = (int) (getHeight() * 0.05);

        g.setColor(Color.RED);

        g.fillRect(xPadding, yPadding,
                horizontalLength, horizontalWidth); // Horizontaler Rand oben links
        g.fillRect(getWidth() - xPadding - horizontalLength, yPadding,
                horizontalLength, horizontalWidth); // Horizontaler Rand oben rechts
        g.fillRect(xPadding, getWidth() - yPadding - horizontalWidth,
                horizontalLength, horizontalWidth); // Horizontaler Rand unten links
        g.fillRect(getWidth() - xPadding - horizontalLength, getHeight() - yPadding - horizontalWidth,
                horizontalLength, horizontalWidth); // Horizontaler Rand unten rechts

        g.fillRect(xPadding, yPadding,
                verticalWidth, verticalLength); // Vertikaler Rand oben links
        g.fillRect(getWidth() - xPadding - verticalWidth, yPadding,
                verticalWidth, verticalLength); // Horizontaler Rand oben rechts
        g.fillRect(xPadding, getWidth() - yPadding - horizontalLength,
                verticalWidth, verticalLength); // Horizontaler Rand unten links
        g.fillRect(getWidth() - xPadding - verticalWidth, getHeight() - yPadding - verticalLength,
                verticalWidth, verticalLength); // Horizontaler Rand unten rechts

    }

    public void setSelectable(boolean selectable)
    {
        repaint();

    }


    public JChessPiece(ChessPieceId id, Boolean isBlack)
    {
;
        this.setOpaque(false);
        URI uri;
        try
        {
            uri = loadUri(id, isBlack);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        setLayout(null);
        svg = new JSVGCanvas();
        svg.setURI(uri.toString());


        svg.setOpaque(false);
        svg.setBackground(new Color(0, 0, 0, 0));

        add(svg);

        this.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                updateSvg();
            }

            public void componentShown(ComponentEvent e)
            {
                updateSvg();
            }
        });


    }


    public static class ResourceHelper
    {
        public static String IdToKaHu(ChessPieceId id)
        {
            // Correspond to the files in resources/ka-hu/chess_kaneo/
            return switch (id)
                    {
                        case KING -> "K";
                        case QUEEN -> "Q";
                        case TOWER -> "R";

                        case BISHOP -> "B";
                        case HORSE -> "N";
                        case PAWN -> "P";
                    };
        }


    }

    static URI loadUri(ChessPieceId id, Boolean isBlack) throws IOException
    {
        var c = (isBlack ? "b" : "w") + ResourceHelper.IdToKaHu(id) + ".svg";
        var res = ClassLoader.getSystemClassLoader().getResource("./ka-hu/chess_kaneo/" + c);
        if (res == null)
            throw new IOException("Could not find a SVG-ressource for " + id.toString() + " in " + (isBlack ? "black" : "white") + " color");
        try
        {
            return res.toURI();
        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

}