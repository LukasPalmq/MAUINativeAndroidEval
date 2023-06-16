using CommunityToolkit.Maui.Alerts;
using CommunityToolkit.Maui.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAUI_exjobb;

public partial class SecondPage : ContentPage
{
    public SecondPage(long startTime)
    {
        InitializeComponent();
        long endTime = DateTime.Now.Ticks;
        long elapsedTime = (endTime - startTime) / TimeSpan.TicksPerMillisecond;
        ShowToast($"{elapsedTime} ms");
    }

    public async void ShowToast(string message)
    {
        MainThread.BeginInvokeOnMainThread(async () =>
        {
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

            ToastDuration duration = ToastDuration.Long;
            double fontSize = 14;

            var toast = Toast.Make(message, duration, fontSize);

            await toast.Show(cancellationTokenSource.Token);
        });
    }
}