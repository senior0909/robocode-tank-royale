//----------------------
// <auto-generated>
//     Generated using the NJsonSchema v10.7.1.0 (Newtonsoft.Json v9.0.0.0) (http://NJsonSchema.org)
// </auto-generated>
//----------------------


namespace Robocode.TankRoyale.Schema
{
    #pragma warning disable // Disable all warnings

    /// <summary>
    /// Bullet state
    /// </summary>
    [System.CodeDom.Compiler.GeneratedCode("NJsonSchema", "10.7.1.0 (Newtonsoft.Json v9.0.0.0)")]
    public class BulletState
    {
        /// <summary>
        /// ID of the bullet
        /// </summary>
        [Newtonsoft.Json.JsonProperty("bulletId", Required = Newtonsoft.Json.Required.Always)]
        public int BulletId { get; set; }

        /// <summary>
        /// ID of the bot that fired the bullet
        /// </summary>
        [Newtonsoft.Json.JsonProperty("ownerId", Required = Newtonsoft.Json.Required.Always)]
        public int OwnerId { get; set; }

        /// <summary>
        /// Bullet firepower (between 0.1 and 3.0)
        /// </summary>
        [Newtonsoft.Json.JsonProperty("power", Required = Newtonsoft.Json.Required.Always)]
        public double Power { get; set; }

        /// <summary>
        /// X coordinate
        /// </summary>
        [Newtonsoft.Json.JsonProperty("x", Required = Newtonsoft.Json.Required.Always)]
        public double X { get; set; }

        /// <summary>
        /// Y coordinate
        /// </summary>
        [Newtonsoft.Json.JsonProperty("y", Required = Newtonsoft.Json.Required.Always)]
        public double Y { get; set; }

        /// <summary>
        /// Direction in degrees
        /// </summary>
        [Newtonsoft.Json.JsonProperty("direction", Required = Newtonsoft.Json.Required.Always)]
        public double Direction { get; set; }

        /// <summary>
        /// Color of the bullet
        /// </summary>
        [Newtonsoft.Json.JsonProperty("color", Required = Newtonsoft.Json.Required.DisallowNull, NullValueHandling = Newtonsoft.Json.NullValueHandling.Ignore)]
        [System.ComponentModel.DataAnnotations.RegularExpression(@"/^#[0-9A-F]{3,6}$/ig")]
        public string Color { get; set; }


    }
}