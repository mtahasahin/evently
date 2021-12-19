import Link from 'next/link';

function HeroSection() {
  return (
    <div className="h-auto h-[500px] bg-blue-100 relative before:bg-gradient-to-r before:from-blue-800 before:to-transparent before:absolute before:inset-0 before:w-full before:h-full before:z-10">
      <div
        className="h-[500px] bg-center bg-no-repeat bg-cover"
        style={{
          backgroundImage: "url('/hero.png')",
        }}
      />
      <div className="absolute inset-0 px-0 px-8 z-[40] flex items-center">
        <div className="w-4/5 sm:container flex mx-auto justify-center items-center">
          <div className="gap-y-10 flex-col flex items-start">
            <div className="text-2xl sm:text-5xl text-white font-bold">
              Organize Events with Evently
            </div>
            <div className="text-xl sm:text-2xl text-white w-full sm:w-2/3">
              Meet Evently: All-in-One platform to easily organize virtual,
              hybrid or in-person events.
            </div>
            <div className="flex gap-x-4">
              <Link href="/create/event">
                <a className="py-2 sm:py-4 px-5 sm:px-7 bg-white hover:bg-gray-200 transition rounded max-w-full font-bold text-blue-900 sm:text-lg">
                  Organize an Event
                </a>
              </Link>
              <Link href="/why">
                <a className="py-2 sm:py-4 px-5 sm:px-7 rounded max-w-full font-bold text-white sm:text-lg border">
                  Learn More
                </a>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default HeroSection;
