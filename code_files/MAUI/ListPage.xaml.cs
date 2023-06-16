using Microsoft.Maui.Controls;

namespace MAUI_exjobb
{
    public partial class ListPage : ContentPage
    {
        public ListPage()
        {
            InitializeComponent();

            BindingContext = new ListPageViewModel();
        }
    }
}