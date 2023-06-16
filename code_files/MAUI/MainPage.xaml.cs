using System.Diagnostics;
using Path = System.IO.Path;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Devices.Sensors;
using System.Threading.Tasks;
using CommunityToolkit.Maui.Alerts;
using CommunityToolkit.Maui.Core;

namespace MAUI_exjobb;

public partial class MainPage : ContentPage
{
    public MainPage()
	{
		InitializeComponent();
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


    private async void OnButtonClicked(object sender, EventArgs e)
    {
        long startTime = DateTime.Now.Ticks;
        await Navigation.PushAsync(new SecondPage(startTime));
    }

    private async Task DownloadFileAsync()
    {
        var url = "https://speed.cloudflare.com/__down?bytes=1048576";
        using var httpClient = new HttpClient();

        try
        {
            var downloadStopwatch = new Stopwatch();
            downloadStopwatch.Start();

            var response = await httpClient.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                // Handle error
                return;
            }

            var data = await response.Content.ReadAsByteArrayAsync();

            downloadStopwatch.Stop();
            //Console.WriteLine($"File download took: {downloadStopwatch.ElapsedMilliseconds} ms");
            ShowToast($"{downloadStopwatch.ElapsedMilliseconds} ms");
            downloadStopwatch.Reset();
        }
        catch (Exception ex)
        {
            // Handle exceptions
        }
    }

    public async Task GetLocationTimeAsync()
    {
        await MainThread.InvokeOnMainThreadAsync(async () =>
        {
            try
            {
                var permissionStatus = await Permissions.CheckStatusAsync<Permissions.LocationWhenInUse>();

                if (permissionStatus != PermissionStatus.Granted)
                {
                    permissionStatus = await Permissions.RequestAsync<Permissions.LocationWhenInUse>();
                }

                if (permissionStatus == PermissionStatus.Granted)
                {
                    var stopwatch = new Stopwatch();
                    stopwatch.Start();

                    var request = new GeolocationRequest(GeolocationAccuracy.Best);
                    var location = await Geolocation.GetLocationAsync(request);

                    stopwatch.Stop();

                    if (location != null)
                    {
                        //Console.WriteLine($"Latitude: {location.Latitude}, Longitude: {location.Longitude}, Time taken: {stopwatch.ElapsedMilliseconds} ms");
                        ShowToast($"Time taken: {stopwatch.ElapsedMilliseconds} ms");
                        stopwatch.Reset();
                    }
                }
                else
                {
                    Console.WriteLine("Location permission is not granted.");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
        });
    }


    public async Task FileReadWriteOperationsAsync()
    {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.Start();
        for (int i = 0; i < 100; i++)
        {
            var filePath = Path.Combine(FileSystem.CacheDirectory, "largeFile.txt");

            var data = new byte[1024 * 1024]; // 1 MB of data

            // Write to file
            await File.WriteAllBytesAsync(filePath, data);

            // Read from file
            var _ = await File.ReadAllBytesAsync(filePath);

            // Delete file
            File.Delete(filePath);
        }
        stopwatch.Stop();
        ShowToast($"{stopwatch.ElapsedMilliseconds} ms");
        stopwatch.Reset();
    }

    private async Task PerformMultithreadedTaskAsync()
    {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.Start();

        for (int x = 0; x < 50; x++)
        {
            int iterations = 1_000_000; // Number of iterations to approximate pi
            var tasks = new List<Task<double>>();

            double ApproximatePi(int iterations)
            {
                double sum = 0;
                for (int i = 0; i < iterations; i++)
                {
                    double term = Math.Pow(-1, i) / (2 * i + 1);
                    sum += term;
                }
                return sum * 4;
            }



            for (int i = 0; i < 10; i++)
            {
                tasks.Add(Task.Run(() =>
                {
                    double pi = ApproximatePi(iterations);
                    return pi;
                }));
            }

            await Task.WhenAll(tasks);
        }
        stopwatch.Stop();
        ShowToast($"{stopwatch.ElapsedMilliseconds} ms");
        stopwatch.Reset();
    }


    private void OnCalculateGpsClicked(object sender, EventArgs e)
    {
        Task.Run(() => GetLocationTimeAsync());
    }


    private void OnFileReadWriteClicked(object sender, EventArgs e)
    {
        Task.Run(() => FileReadWriteOperationsAsync());
    }

    private void OnMultithreadedTaskClicked(object sender, EventArgs e)
    {
        Task.Run(() => PerformMultithreadedTaskAsync());
    }

    private void OnButtonDownloadClicked(object sender, EventArgs e)
    {
        Task.Run(() => DownloadFileAsync());
    }
}

