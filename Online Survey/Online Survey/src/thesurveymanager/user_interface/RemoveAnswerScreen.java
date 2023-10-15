package thesurveymanager.user_interface;

import javax.swing.JPanel;

public class RemoveAnswerScreen extends JPanel{
    private UserInterface frame;
    
    public int id;
    
    private boolean isValid;
    
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton ConfirmButton;
    private javax.swing.JTextField ErrorTextField;
    private javax.swing.JTextField IdLabelTextField;
    private javax.swing.JTextField IdTextField;
    
    public RemoveAnswerScreen(UserInterface frame) {
        this.frame = frame;
        initComponents();
    }
    
    private void initComponents() {
        IdLabelTextField = new javax.swing.JTextField();
        IdLabelTextField.setBackground(new java.awt.Color(204, 204, 204));
        IdLabelTextField.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        IdLabelTextField.setText("ID:");
        IdLabelTextField.setEditable(false);
        
        IdTextField = new javax.swing.JTextField();
        IdTextField.setFont(new java.awt.Font("Tahoma", 0, 24));
        
        ConfirmButton = new javax.swing.JButton();
        ConfirmButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        ConfirmButton.setText("COFIRM");
        ConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmButtonActionPerformed(evt);
            }
        });
        
        CancelButton = new javax.swing.JButton();
        CancelButton.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CancelButton.setText("CANCEL");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        
        ErrorTextField = new javax.swing.JTextField();
        ErrorTextField.setBackground(getBackground());
        ErrorTextField.setForeground(new java.awt.Color(255, 0, 0));
        ErrorTextField.setBorder(null);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IdLabelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(151, 151, 151))
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ErrorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addComponent(ConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(IdLabelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(ErrorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }
    
    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
     frame.showHistoryScreen();
    }                                            

    private void ConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        isValid = true;

        try {
               id = Integer.parseInt(IdTextField.getText());
            } catch (Exception e) {
                System.out.println(e);
                ErrorTextField.setText("Please enter a valid response ID");
                isValid = false;
            }

        if(id <= 0) {
            ErrorTextField.setText("Please enter a valid response ID");
            isValid = false;
        }

        if(isValid) {
            frame.removeAnswerOperation();
            frame.showHistoryScreen();
        }
    } 
}
