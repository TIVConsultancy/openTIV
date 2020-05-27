/* 
 * Copyright 2020 TIVConsultancy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package delete.com.tivconsultancy.opentiv.devgui.frames;

import com.tivconsultancy.opentiv.imageproc.img_io.IMG_Reader;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author Thomas Ziegenhein
 */
public class Frame_StartUp extends JInternalFrame {

    public JLabel jLabelLogo = new JLabel();
    public JLabel jLabelText = new JLabel();
    public JLabel jLabelGif = new JLabel();

    public Frame_StartUp() {
        super("StartUp");

        setIconifiable(false);
        setMaximizable(false);
        setEnabled(true);
        setResizable(false);
        setClosable(false);
        this.setBorder(null);

//        setBackground(Color.red);

        jLabelLogo.setIcon(new ImageIcon(getClass().getResource("/Logo/LoadingLogo.gif")));

        this.add(jLabelLogo);
        this.add(jLabelText);
        this.add(jLabelGif);

        setSize(550, 350);
        setLayout();

        setVisible(true);

    }

    private void setLayout() {

//        this.setLayout(new BorderLayout(20, 20));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jLabelLogo, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jLabelText, c);

        c = new GridBagConstraints();
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1; //GridBagConstraints.REMAINDER;
        c.gridheight = 1; //GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        ((GridBagLayout) this.getContentPane().getLayout()).setConstraints(jLabelGif, c);

    }

}
