import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.lang.Runtime;
import java.lang.Process;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * ʵ�ֱ����ļ������Ϊ�̳�JPanel��һ�����
 *
 * @author Lonsy
 * @version 1.0
 */
public class LocalFile extends JPanel implements ActionListener, MouseListener
{
    private JButton jbUp;
    private JComboBox jcbPath;
    private JTable jtFile;
    private DefaultTableModel dtmFile;
    private JLabel jlLocal;
    private File path;
    private String currentPath;
    private int currentIndex;
    private boolean init = false;

    public LocalFile() {
        super(new BorderLayout());
        
        JPanel jp = new JPanel(new BorderLayout());
        jbUp = new JButton("Up");
        jbUp.addActionListener(this);
        jcbPath = new JComboBox();
        jcbPath.addActionListener(this);
        jp.add(jbUp, "West");
        jp.add(jcbPath, "Center");
        dtmFile = new LocalTableModel();
        dtmFile.addColumn("����");
        dtmFile.addColumn("��С");
        dtmFile.addColumn("����");
        dtmFile.addColumn("�޸�����");
        jtFile = new JTable(dtmFile);
        jtFile.setShowGrid(false);
        jtFile.addMouseListener(this);
        jlLocal = new JLabel("����״̬", JLabel.CENTER);

        add(jp, "North");
        add(new JScrollPane(jtFile), "Center");
        add(jlLocal, "South");

        //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
        path = new File(System.getProperty("user.dir"));
        listFiles(path);    

        init = true;
    }

    //����·����ѡ���¼�
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==jbUp && jtFile.getValueAt(0, 0).toString().equals("�����ϼ�")
                && jtFile.getValueAt(0, 2).toString().equals(""))
        {
            listFiles(new File(currentPath).getParentFile());
            return;
        }
        if (init == false)
        {
            return;
        }
        int index = jcbPath.getSelectedIndex();
        String item = (String)jcbPath.getSelectedItem();
        if (item.startsWith("  "))
        {
            int root = index - 1;
            while (((String)jcbPath.getItemAt(root)).startsWith("  "))
            {
                root--;
            }
            String path = (String)jcbPath.getItemAt(root);
            while (root < index)
            {
                path += ((String)jcbPath.getItemAt(++root)).trim();;
                path += "\\";
            }
            if (listFiles(new File(path)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
        else
        {
            if (listFiles(new File(item)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
    }

    //JTable���ļ���˫���¼�
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==2) {
            int row = ((JTable)e.getSource()).getSelectedRow();
            if (((JTable)e.getSource()).getValueAt(row, 2).toString().equals("�ļ���"))
            {
                File file;
                //�ж��Ƿ�Ϊ��Ŀ¼������ͬ����һ�� \ �Ĳ��
                if (currentPath.split("\\\\").length > 1)
                {
                    file = new File(currentPath + "\\" + ((JTable)e.getSource()).getValueAt(row, 0).toString());
                }
                else
                {                    
                    file = new File(currentPath + ((JTable)e.getSource()).getValueAt(row, 0).toString());
                }
                listFiles(file);
            }
            else if (((JTable)e.getSource()).getValueAt(row, 0).toString().equals("�����ϼ�")
                    && ((JTable)e.getSource()).getValueAt(row, 2).toString().equals(""))
            {
                listFiles(new File(currentPath).getParentFile());
            }
        }
    }
    //����һ�����õ��¼�
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
    private boolean listFiles(File path) {
        String strPath = path.getAbsolutePath();
        if (path.isDirectory() == false)
        {
            JOptionPane.showMessageDialog(this, "��·�������ڣ����޴��ļ�");
            return false;
        }
        
        currentPath = path.getAbsolutePath();
        init = false;
        jcbPath.removeAllItems();
        File[] roots = File.listRoots();
        int index = 0;
        for (int i=0; i<roots.length; i++)
        {
            String rootPath = roots[i].getAbsolutePath();
            jcbPath.addItem(rootPath);
            if (currentPath.indexOf(rootPath) != -1)
            {
                String[] bufPath = currentPath.split("\\\\");
                for (int j=1; j<bufPath.length; j++)
                {
                    String buf = "  ";
                    for (int k=1; k<j; k++)
                    {
                        buf += "  ";
                    }
                    jcbPath.addItem(buf + bufPath[j]);
                    index = i + j;
                }
                if (bufPath.length == 1)
                {
                    index = i;
                }
            }
        }
        jcbPath.setSelectedIndex(index);
        init = true;
        currentIndex = index;

        //�����������
        dtmFile.setRowCount(0);

        //����ж�Ϊ�Ƿ�����Ŀ¼������� �����ϼ� һ��
        if (strPath.split("\\\\").length > 1)
        {
            dtmFile.addRow(new String[]{"�����ϼ�", "", "", ""});
        }

        //�г���ǰĿ¼����Ŀ¼���ļ�
        File[] files = path.listFiles();
        for (int i=0; i<files.length; i++)
        {
            String name = files[i].getName();
            if (files[i].isDirectory())
            {
                dtmFile.addRow(new String[]{name, "", "�ļ���", ""});
            }
            else
            {
                if (name.lastIndexOf(".") != -1)
                {
                    dtmFile.addRow(new String[]{name.substring(0, name.lastIndexOf(".")), 
                            sizeFormat(files[i].length()), 
                            name.substring(name.lastIndexOf(".") + 1),
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
                else
                {
                    dtmFile.addRow(new String[]{name, 
                            sizeFormat(files[i].length()), 
                            "",
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
            }
        }
        
        jlLocal.setText(currentPath);

        return true;
    }

    //���ļ���Сת������Ӧ�ַ�����ʽ
    private String sizeFormat(long length) {
        long kb;
        if (length < 1024)
        {
            return String.valueOf(length);
        }
        else if ((kb = length / 1024) < 1024)
        {
            return (String.valueOf(kb) + "kb");
        }
        else
        {
            return (String.valueOf(length / 1024 / 1024) + "kb");
        }
    }

    //����
    public static void main(String[] args) {
        JFrame jf = new JFrame("����");
        jf.setSize(300, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation((int)(di.getWidth() - jf.getWidth()) / 2, 
                (int)(di.getHeight() - jf.getHeight()) / 2);
        jf.add(new LocalFile());
        jf.setVisible(true);
    }

    //ʵ����Ӧ��tablemodel��
    class LocalTableModel extends DefaultTableModel
    {
        public boolean isCellEditable(int row, int column) {
            return false;
        }  
    }
}