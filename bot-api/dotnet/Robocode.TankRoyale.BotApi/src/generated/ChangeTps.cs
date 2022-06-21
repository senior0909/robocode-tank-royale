//----------------------
// <auto-generated>
//     Generated using the NJsonSchema v10.7.1.0 (Newtonsoft.Json v9.0.0.0) (http://NJsonSchema.org)
// </auto-generated>
//----------------------


namespace Robocode.TankRoyale.Schema
{
    #pragma warning disable // Disable all warnings

    /// <summary>
    /// Command to change the TPS (Turns Per Second), which is the number of turns displayed for an observer. TPS is similar to FPS, where a frame is equal to a turn.
    /// </summary>
    [System.CodeDom.Compiler.GeneratedCode("NJsonSchema", "10.7.1.0 (Newtonsoft.Json v9.0.0.0)")]
    public class ChangeTps : Message
    {
        /// <summary>
        /// Turns per second (TPS). Typically a value from 0 to 999. -1 means maximum possible TPS speed.
        /// </summary>
        [Newtonsoft.Json.JsonProperty("tps", Required = Newtonsoft.Json.Required.DisallowNull, NullValueHandling = Newtonsoft.Json.NullValueHandling.Ignore)]
        public int? Tps { get; set; }


    }
}