using System.Collections.ObjectModel;

namespace MAUI_exjobb
{
    public class ListPageViewModel
    {
        public ObservableCollection<string> Items { get; set; }

        public ListPageViewModel()
        {
            Items = new ObservableCollection<string>();

            for (int i = 1; i <= 1000; i++)
            {
                Items.Add($"Item {i}");
            }
        }
    }
}